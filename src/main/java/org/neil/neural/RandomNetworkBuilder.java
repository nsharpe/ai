package org.neil.neural;

import org.neil.neural.input.XDirectionInput;
import org.neil.neural.input.YDirectionInput;
import org.neil.neural.output.LeftDirectionOutput;
import org.neil.neural.output.MoveOutput;
import org.neil.neural.output.RightDirectionOutput;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomNetworkBuilder {
    private static Random random = new Random();

    private List<Input> inputs;
    private List<Output> outputs;
    private int minNodes = 0;
    private int maxNodes = 5;
    private int minConnection = 0;
    private int maxConnection = 20;
    private int minStorage = 0;
    private int maxStorage = 128;
    private int minBandwith = 0;
    private int maxBandwith = 64;
    private int bandwidthModificationIncrements = 10;
    private double mutationRate = 0.015;

    public RandomNetworkBuilder() {
        inputs = new ArrayList<>();
        inputs.add(new XDirectionInput(256));
        inputs.add(new YDirectionInput(256));
        //inputs.add(new XPositionInput(512));

        outputs = new ArrayList<>();
        outputs.add(new LeftDirectionOutput(32));
        outputs.add(new RightDirectionOutput(32));
        outputs.add(new MoveOutput(32));
    }

    public Network build() {
        validate();
        List<Node> nodes = generateNodes();

        return new Network(nodes, generateConnections(nodes));

    }

    public Node createIntermediateNode(Collection<Node> currentNodes) {
        return createIntermediateNode(currentNodes.stream()
                .mapToInt(x -> x.getId())
                .max().orElse(1000) + 1);
    }

    public Node createIntermediateNode(int id) {
        int capacity = randomRange(minStorage, maxStorage);
        switch (random.nextInt(4)){
            case 0:
                return new NodeDefault(id, capacity);
            case 1:
                return new NodeMultiplier(id, capacity);
            case 2:
                return new NodeDivisor(id, capacity);
            case 3:
                return new NodeSink(id);
        }
        throw new IllegalStateException("createIntermediateNode function broken");
    }

    public Network copyWithChanceToMutate(Network network) {
        List<Node> inputs = network.getInputs().stream().map(x -> x.copy()).collect(Collectors.toList());
        List<Node> outputs = network.getOutputs().stream().map(x -> x.copy()).collect(Collectors.toList());
        List<Node> intermediate = network.getIntermediateNodes().stream().map(x -> x.copy()).collect(Collectors.toList());

        MutationType mutation = MutationType.random(mutationRate);
        mutation = random.nextBoolean() ? MutationType.CONNECTION_ADD : mutation;

        if (mutation == MutationType.NODE_REMOVAL) {
            if (!intermediate.isEmpty()) {
                intermediate.remove(random.nextInt(intermediate.size()));
            }
        } else if (mutation == MutationType.NODE_ADD) {
            if (intermediate.size() < maxNodes) {
                intermediate.add(createIntermediateNode(intermediate));
            }
        }

        List<Node> allNodes = Stream.concat(Stream.concat(inputs.stream(), outputs.stream()), intermediate.stream())
                .collect(Collectors.toList());

        List<Connection> connections = network.getConnections()
                .map(x -> copyWithChanceToMutate(allNodes, x))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (mutation == MutationType.CONNECTION_ADD) {
            if (connections.size() < maxConnection) {
                List<Node> sources = Stream.concat(inputs.stream(), intermediate.stream())
                        .collect(Collectors.toList());
                List<Node> destinations = Stream.concat(outputs.stream(), intermediate.stream())
                        .collect(Collectors.toList());

                Node source = randomEntry(sources);
                Node destination = randomEntry(destinations);

                connections.add(new Connection(source, destination, randomRange(minBandwith, maxBandwith)));
            }
        } else if (mutation == MutationType.CONNECTION_REMOVAL) {
            if (!connections.isEmpty()) {
                connections.remove(random.nextInt(connections.size()));
            }
        } else if(mutation == MutationType.ADD_CONNECTION_BANDWIDTH){
            Connection toModify = connections.get(random.nextInt(connections.size()));
            connections.remove(toModify);
            connections.add(toModify.copyModifyBandWidth(bandwidthModificationIncrements));
        } else if(mutation == MutationType.REDUCE_CONNECTION_BANDWIDTH){
            Connection toModify = connections.get(random.nextInt(connections.size()));
            if(toModify.getBandwith() > bandwidthModificationIncrements ) {
                connections.remove(toModify);
                connections.add(toModify.copyModifyBandWidth(-bandwidthModificationIncrements));
            }
        }

        return new Network(allNodes, connections);
    }

    private Connection copyWithChanceToMutate(List<Node> nodes, Connection toCopy) {
        Node source = nodes.stream()
                .filter(x -> toCopy.getSource().getId() == x.getId())
                .findAny().orElse(null);

        if (source == null) {
            return null;
        }

        Node destination = nodes.stream()
                .filter(x -> toCopy.getDestination().getId() == x.getId())
                .findAny().orElse(null);

        if (destination == null) {
            return null;
        }

        return new Connection(source, destination, toCopy.getBandwith());
    }

    private enum MutationType {
        NODE_ADD,
        NODE_REMOVAL,
        CONNECTION_ADD,
        CONNECTION_REMOVAL,
        ADD_CONNECTION_BANDWIDTH,
        REDUCE_CONNECTION_BANDWIDTH,
        NONE;
        private static Random random = new Random();

        public static MutationType random(double mutationRate) {
            if (random.nextDouble() > mutationRate) {
                return NONE;
            }
            return MutationType.values()[random.nextInt(MutationType.values().length)];
        }
    }

    private List<Node> generateNodes() {
        List<Node> nodes = new ArrayList<>();
        int numOfNodes = randomRange(minNodes, maxNodes);
        for (int i = 0; i <= numOfNodes; i++) {
            int storage = randomRange(minStorage, maxStorage);

            nodes.add(new NodeDefault(i + 1000, storage));
        }

        nodes.addAll(inputs.stream().map(x -> x.copy()).collect(Collectors.toList()));
        nodes.addAll(outputs.stream().map(x -> x.copy()).collect(Collectors.toList()));
        return nodes;
    }

    private static int randomRange(int min, int max) {
        return min + random.nextInt(max - min);
    }

    private List<Connection> generateConnections(Collection<Node> intermediateNodes) {
        List<Node> sources = Stream.concat(inputs.stream(), intermediateNodes.stream())
                .collect(Collectors.toList());
        List<Node> destinations = Stream.concat(outputs.stream(), intermediateNodes.stream())
                .collect(Collectors.toList());

        List<Connection> connections = new ArrayList<>();

        int numberOfConnections = randomRange(minConnection, maxConnection);

        for (int i = 0; i < numberOfConnections; i++) {
            Node source = randomEntry(sources);
            Node destination = randomEntry(destinations);

            connections.add(new Connection(source, destination, randomRange(minBandwith, maxBandwith)));
        }
        return connections;
    }

    public static Node randomEntry(List<Node> collection) {
        return collection.get(random.nextInt(collection.size()));
    }

    public void validate() {
        Objects.requireNonNull(inputs, "input");
        Objects.requireNonNull(outputs, "outputs");
    }

    public RandomNetworkBuilder inputs(List<Input> inputs) {
        this.inputs = inputs;
        if (inputs.isEmpty()) {
            throw new IllegalStateException("inputs can't be empty");
        }
        return this;
    }

    public RandomNetworkBuilder outputs(List<Output> outputs) {
        this.outputs = outputs;
        if (outputs.isEmpty()) {
            throw new IllegalStateException("outputs can't be empty");
        }
        return this;
    }

    public RandomNetworkBuilder minNodes(int minNodes) {
        this.minNodes = minNodes;
        return this;
    }

    public RandomNetworkBuilder maxNodes(int maxNodes) {
        this.maxNodes = maxNodes;
        return this;
    }

    public RandomNetworkBuilder minConnection(int minConnection) {
        this.minConnection = minConnection;
        return this;
    }

    public RandomNetworkBuilder maxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
        return this;
    }

    public RandomNetworkBuilder minStorage(int minStorage) {
        this.minStorage = minStorage;
        return this;
    }

    public RandomNetworkBuilder maxStorage(int maxStorage) {
        this.maxStorage = maxStorage;
        return this;
    }

    public RandomNetworkBuilder minBandwith(int minBandwith) {
        this.minBandwith = minBandwith;
        return this;
    }

    public RandomNetworkBuilder maxBandwith(int maxBandwith) {
        this.maxBandwith = maxBandwith;
        return this;
    }
}
