package org.neil.neural;

import org.neil.neural.input.InputNode;
import org.neil.neural.output.OutputNode;
import org.neil.simulation.SimulationInput;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomNetworkBuilder<I,O>{
    private static Random random = new Random();

    private List<InputNode> inputNodes;
    private List<OutputNode> outputNodes;
    private int minNodes = 1;
    private final int maxNodes;
    private int minConnection = 2;
    private final int maxConnection;
    private int minStorage = 1;
    private int minBandwith = 0;
    private int maxBandwith = 512;
    private int maxStorage = maxBandwith * 4;

    private int maxActivation = maxStorage / 2;
    private int bandwidthModificationIncrements = 10;
    private volatile double mutationRate;

    private final List<BiFunction<Integer, Integer, Node>> nodeSupplier = List.of(
            (id, capacity) -> new MutateableNodeDefault(id, capacity, capacity / 2),
            (id, capacity) -> new NodeMultiplier(id, capacity, capacity / 2),
            (id, capacity) -> new NodeDivisor(id, capacity, capacity / 2),
            (id, capacity) -> new NodeSink(id),
            (id, capacity) -> new NodeAlwaysFull(id, capacity),
            (id, capacity) -> new NodeAlwaysEmpty(id, capacity)
    );

    public RandomNetworkBuilder(SimulationInput simulationInput) {
        this.mutationRate = simulationInput.mutationRate;
        this.maxNodes = simulationInput.maxNumberOfNodes;
        this.maxConnection = simulationInput.maxNumberOfConnections;

        // The safe copying of lists is to protect against accidental duplication
        // Review later.  I might not be smart enough to not make that mistake?
        inputNodes = simulationInput.inputNodeGenerator.inputs(1).stream().toList();
        outputNodes = simulationInput.outputNodeGenerator.outputs(inputNodes.size()+1).stream().toList();
    }

    public Network build() {
        validate();
        List<Node> nodes = generateIntermediateNodes();

        List<InputNode> inputNodes = this.inputNodes.stream().map(x->x.copy()).toList();
        List<OutputNode> outputNodes = this.outputNodes.stream().map(x->x.copy()).toList();

        return  new Network(inputNodes,
                outputNodes,
                nodes,
                generateConnections(inputNodes,outputNodes,nodes));

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
        List<InputNode> inputs = network.getInputs().stream().map(x -> x.copy()).collect(Collectors.toList());
        List<OutputNode> outputs = network.getOutputs().stream().map(x -> x.copy()).collect(Collectors.toList());
        List<Node> intermediate = network.getIntermediateNodes().stream().map(x -> x.copy()).collect(Collectors.toList());

        final MutationType mutation = MutationType.random(mutationRate);

        List<Connection> connections = network.streamConnections()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (mutation == MutationType.NODE_REMOVAL
                && intermediate.size() > minNodes) {
            Node toRemove = randomEntry(intermediate);
            // Only remove node if it won't bring the number of connections bellow the minimum
            if(intermediate.size() - network.connectionsBelongingTo(toRemove).count() > minConnection){
                intermediate.remove(toRemove);
                //remove connections associated with the node
                connections = connections.stream()
                        .filter( x -> x.getSource().getId() != toRemove.getId())
                        .filter( x -> x.getDestination().getId() != toRemove.getId())
                        .collect(Collectors.toList());
            }
        } else if (mutation == MutationType.MUTATE_NODE) {
            Node toModify = randomEntry(intermediate.stream()
                    .filter( x -> x instanceof MutateableNode)
                    .collect(Collectors.toList()));
            if(toModify != null){
                MutateableNode mutate = ((MutateableNode) toModify).mutate(minStorage,maxStorage,maxActivation);
                intermediate = updateNode(intermediate,mutate);
            }
        } else if (mutation == MutationType.NODE_TYPE && !intermediate.isEmpty()){
            Node toModify = randomEntry(intermediate);
            intermediate = updateNode(intermediate,createIntermediateNode(toModify.getId(), toModify.getCapacity()) );
        } else if (mutation == MutationType.NODE_RANDOMIZE_CAPACITY && !intermediate.isEmpty()) {
            Node toModify = randomEntry(intermediate);
            intermediate.remove(toModify);
            intermediate.add(createIntermediateNode(toModify.getId()));
        }else if (mutation == MutationType.CONNECTION_ADD && connections.size() < maxConnection) {
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

        } else if (mutation == MutationType.CONNECTION_REMOVAL && connections.size() > minConnection) {
            connections.remove(random.nextInt(connections.size()));

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
        } else if (mutation == MutationType.RANDOMIZE_CONNECTION_BANDWIDTH && connections.size() > minConnection) {
            Connection toModify = connections.get(random.nextInt(connections.size()));
            connections.remove(toModify);
            connections.add(toModify.copyNewBandWidth(random.nextInt(maxBandwith)));
        } else if (mutation == MutationType.SHUFFLE_CONNECTION_PRIORITY && connections.size() > 1) {
            Collections.shuffle(connections);
        }else if (mutation == MutationType.SHUFFLE_ALL_CONNECTIONS && connections.size() > minConnection) {
            List<Node> sources = Stream.concat(inputs.stream(), intermediate.stream())
                    .collect(Collectors.toList());
            List<Node> destinations = Stream.concat(outputs.stream(), intermediate.stream())
                    .collect(Collectors.toList());

            connections = connections.stream()
                            .map( x ->new Connection(randomEntry(sources),
                                    randomEntry(destinations),
                                    x.getBandwith(),
                                    x.getConnectionType()) )
                    .toList();
        } else if (mutation == MutationType.CONNECTION_TO_NODE
                && intermediate.size() <= maxNodes
                && connections.size() <= maxConnection
                && connections.size() > 1) {
            Node toAdd = createIntermediateNode(intermediate);
            intermediate.add(toAdd);

            int index = random.nextInt(connections.size());
            Connection toModify = connections.get(random.nextInt(connections.size()));
            connections.remove(index);

            connections.add(new Connection(toModify.getSource(), toAdd,
                    randomRange(minBandwith, maxBandwith),
                    Connection.ConnectionType.random()));
            connections.add(new Connection(toAdd, toModify.getDestination(),
                    randomRange(minBandwith, maxBandwith),
                    Connection.ConnectionType.random()));
        }
        else if (mutation == MutationType.SHUFFLE_CONNECTION && connections.size() > 1) {
            List<Node> sources = Stream.concat(inputs.stream(), intermediate.stream())
                    .collect(Collectors.toList());
            List<Node> destinations = Stream.concat(outputs.stream(), intermediate.stream())
                    .collect(Collectors.toList());

            int connectionIndex = random.nextInt(connections.size());
            Connection toModify = connections.get(connectionIndex);
            connections.remove(connectionIndex);

            connections.add(new Connection(randomEntry(sources),
                            randomEntry(destinations),
                            toModify.getBandwith(),
                            toModify.getConnectionType()) );
        }

        if(intermediate.size()<5 && network.getIntermediateNodes().stream().count()>=5
        && mutation != MutationType.NODE_REMOVAL) {
            System.out.println(mutation);
        }

        return new Network(inputs,
                outputs,
                intermediate,
                connections);
    }

    private static List<Node> updateNode(List<Node> nodes, Node toUpdate){
        return Stream.concat(nodes.stream()
                .filter(x->x.getId() != toUpdate.getId()),Stream.of(toUpdate)).toList();
    }

    private enum MutationType {
        NODE_REMOVAL,
        MUTATE_NODE,
        NODE_TYPE,
        NODE_RANDOMIZE_CAPACITY,
        CONNECTION_TO_NODE,
        CONNECTION_ADD,
        CONNECTION_REMOVAL,
        ADD_CONNECTION_BANDWIDTH,
        REDUCE_CONNECTION_BANDWIDTH,
        RANDOMIZE_CONNECTION_BANDWIDTH,
        SHUFFLE_CONNECTION_PRIORITY,
        SHUFFLE_ALL_CONNECTIONS,
        SHUFFLE_CONNECTION,
        NONE;
        private static Random random = new Random();

        public static MutationType random(double mutationRate) {
            if (random.nextDouble() > mutationRate) {
                return NONE;
            }
            return MutationType.values()[random.nextInt(MutationType.values().length)];
        }
    }

    private List<Node> generateIntermediateNodes() {
        List<Node> nodes = new ArrayList<>();

        // Always start with the minimum number of nodes.  Done so networks are generated from simplest to more complicated
        for (int i = 0; i <= minNodes; i++) {
            int storage = randomRange(minStorage, maxStorage);

            nodes.add(new MutateableNodeDefault(i + 1000, storage, 10));
        }

        return nodes;
    }

    private static int randomRange(int min, int max) {
        if (min == max) {
            return min;
        }
        return min + random.nextInt(max - min);
    }

    private List<Connection> generateConnections(Collection<InputNode> inputNodes,
                                                 Collection<OutputNode> outputNodes,
                                                 Collection<Node> intermediateNodes) {
        List<Node> sources = Stream.concat(inputNodes.stream(), intermediateNodes.stream())
                .collect(Collectors.toList());
        List<Node> destinations = Stream.concat(outputNodes.stream(), intermediateNodes.stream())
                .collect(Collectors.toList());

        List<Connection> connections = new ArrayList<>();

        // Always start with the minimum number of connections.  This is done as complex models may start with weird behavior
        for (int i = 0; i < minConnection; i++) {
            Node source = randomEntry(sources);
            Node destination = randomEntry(destinations);

            connections.add(new Connection(source,
                    destination,
                    randomRange(minBandwith, maxBandwith),
                    Connection.ConnectionType.random()));
        }
        return connections;
    }

    private static Node randomEntry(List<Node> collection) {
        if(collection.isEmpty()){
            return null;
        }
        return collection.get(random.nextInt(collection.size()));
    }

    public void validate() {
        Objects.requireNonNull(inputNodes, "input");
        Objects.requireNonNull(outputNodes, "outputs");
    }

    public RandomNetworkBuilder minNodes(int minNodes) {
        this.minNodes = minNodes;
        return this;
    }

    public RandomNetworkBuilder minConnection(int minConnection) {
        this.minConnection = minConnection;
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

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }
}
