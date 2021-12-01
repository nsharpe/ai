package org.neil.neural;

import org.neil.neural.input.MovementBlockedInput;
import org.neil.neural.input.ProximityInput;
import org.neil.neural.input.XDirectionInput;
import org.neil.neural.input.XPositionInput;
import org.neil.neural.input.YDirectionInput;
import org.neil.neural.output.LeftDirectionOutput;
import org.neil.neural.output.MoveOutput;
import org.neil.neural.output.RightDirectionOutput;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomNetworkBuilder {
    private static Random random = new Random();

    private List<Input> inputs;
    private List<Output> outputs;
    private int minNodes = 1;
    private int maxNodes = 20;
    private int minConnection = 1;
    private int maxConnection = 80;
    private int minStorage = 0;
    private int maxStorage = 512;
    private int minBandwith = 0;
    private int maxBandwith = 128;
    private int bandwidthModificationIncrements = 10;
    private double mutationRate = 0.015;

    private final List<BiFunction<Integer, Integer, Node>> nodeSupplier = List.of(
            (id, capacity) -> new NodeDefault(id, capacity),
            (id, capacity) -> new NodeMultiplier(id, capacity),
            (id, capacity) -> new NodeDivisor(id, capacity),
            (id, capacity) -> new NodeSink(id),
            (id, capacity) -> new NodeAlwaysFull(id, capacity),
            (id, capacity) -> new NodeCapacityPastThreshold(id, capacity, randomRange(this.minStorage, capacity))
    );

    public RandomNetworkBuilder() {
        inputs = new ArrayList<>();
        inputs.add(new XDirectionInput(256));
        inputs.add(new YDirectionInput(256));
        inputs.add(new XPositionInput(256));
        inputs.add(new ProximityInput(256));
        inputs.add(new MovementBlockedInput(256));
        //inputs.add(new DirectionViewInput(256));


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
        return createIntermediateNode(id, randomRange(minStorage, maxStorage));
    }

    public Node createIntermediateNode(int id, int capacity) {
        return nodeSupplier.get(random.nextInt(nodeSupplier.size())).apply(id, capacity);
    }

    public Network copyWithChanceToMutate(Network network) {
        List<Node> inputs = network.getInputs().stream().map(x -> x.copy()).collect(Collectors.toList());
        List<Node> outputs = network.getOutputs().stream().map(x -> x.copy()).collect(Collectors.toList());
        List<Node> intermediate = network.getIntermediateNodes().stream().map(x -> x.copy()).collect(Collectors.toList());

        final MutationType mutation = MutationType.random(mutationRate);

        if (mutation == MutationType.NODE_REMOVAL) {
            if (intermediate.size() > minNodes) {
                intermediate.remove(random.nextInt(intermediate.size()));
            }
        } else if (mutation == MutationType.NODE_ADD) {
            if (intermediate.size() < maxNodes) {
                intermediate.add(createIntermediateNode(intermediate));
            }
        } else if (mutation == MutationType.NODE_RANDOMIZE_CAPACITY) {

            Node toModify = randomEntry(intermediate);
            intermediate.remove(toModify);
            intermediate.add(createIntermediateNode(toModify.getId()));
        }

        List<Node> allNodes = Stream.concat(Stream.concat(inputs.stream(), outputs.stream()), intermediate.stream())
                .collect(Collectors.toList());

        List<Connection> connections = network.streamConnections()
                .map(x -> copyWithChanceToMutate(allNodes, x))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (mutation == MutationType.CONNECTION_ADD) {
            if (connections.size() < maxConnection) {
                List<Node> sources = Stream.concat(inputs.stream(), intermediate.stream())
                        .collect(Collectors.toList());
                List<Node> destinations = Stream.concat(outputs.stream(), intermediate.stream())
                        .collect(Collectors.toList());

                Node source;
                Node destination;
                do {
                    source = randomEntry(sources);
                    destination = randomEntry(destinations);
                } while (source != destination);

                connections.add(new Connection(source, destination, randomRange(minBandwith, maxBandwith),
                        Connection.ConnectionType.random()));
            }
        } else if (mutation == MutationType.CONNECTION_REMOVAL) {
            if (connections.size() > minConnection) {
                connections.remove(random.nextInt(connections.size()));
            }
        } else if (mutation == MutationType.ADD_CONNECTION_BANDWIDTH && !connections.isEmpty()) {
            Connection toModify = connections.get(random.nextInt(connections.size()));
            connections.remove(toModify);
            connections.add(toModify.copyModifyBandWidth(bandwidthModificationIncrements));
        } else if (mutation == MutationType.REDUCE_CONNECTION_BANDWIDTH && !connections.isEmpty()) {
            Connection toModify = connections.get(random.nextInt(connections.size()));
            if (toModify.getBandwith() > bandwidthModificationIncrements) {
                connections.remove(toModify);
                connections.add(toModify.copyModifyBandWidth(-bandwidthModificationIncrements));
            }
        } else if (mutation == MutationType.SHUFFLE_CONNECTION_PRIORITY) {
            Collections.shuffle(connections);
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

        return new Connection(source, destination, toCopy.getBandwith(), Connection.ConnectionType.random());
    }

    private enum MutationType {
        NODE_ADD,
        NODE_REMOVAL,
        NODE_RANDOMIZE_CAPACITY,
        CONNECTION_ADD,
        CONNECTION_REMOVAL,
        ADD_CONNECTION_BANDWIDTH,
        REDUCE_CONNECTION_BANDWIDTH,
        SHUFFLE_CONNECTION_PRIORITY,
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
        if (min == max) {
            return min;
        }
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

            connections.add(new Connection(source,
                    destination,
                    randomRange(minBandwith, maxBandwith),
                    Connection.ConnectionType.random()));
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
