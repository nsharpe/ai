package org.neil.neural;

import org.neil.neural.input.InputNode;
import org.neil.neural.input.InputNodeGenerator;
import org.neil.neural.output.OutputNode;
import org.neil.neural.output.OutputNodeGenerator;
import org.neil.simulation.MutationStrategy;
import org.neil.simulation.SimulationInput;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

public final class RandomNetworkBuilder implements Serializable  {
    private static Random random = new Random();

    @Serial
    private final static long serialVersionUID = -1318841703743764474L;


    private final InputNodeGenerator inputs;
    private final OutputNodeGenerator outputs;
    private int minNodes = 1;
    private int maxNodes = 10;
    private int minConnection = 1;
    private int maxConnection = 100;
    private int minStorage = 10;
    private int minBandwith = 1;
    private int maxBandwith = 512;
    private int maxStorage = maxBandwith * 4;

    private int maxActivation = maxStorage / 2;
    private int bandwidthModificationIncrements = 10;

    private int maxNumberOfMutations = 1;
    private volatile double mutationRate;
    private volatile MutationStrategy mutationStrategy = MutationStrategy.ALWAYS_ALLOW;

    private final List<NodeMutator<?>> nodeSupplier = List.of(
            MutateableNodeDefault.mutator(),
            NodeMultiplier.mutator(),
            NodeDivisor.mutator(),
            NodeMax.mutator(),
            NodeAlwaysFull.mutator(),
            NodeAlwaysEmpty.mutator(),
            DifferentialNode.mutator()
    );

    public RandomNetworkBuilder(SimulationInput simulationInput) {

        // The safe copying of lists is to protect against accidental duplication
        //TODO: Review later.  I might not be smart enough to not make that mistake?
        this(simulationInput.inputNodeGenerator,simulationInput.outputNodeGenerator);
    }

    public RandomNetworkBuilder(InputNodeGenerator inputs, OutputNodeGenerator outputs) {

        // The safe copying of lists is to protect against accidental duplication
        //TODO: Review later.  I might not be smart enough to not make that mistake?
        this.inputs = Objects.requireNonNull(inputs);
        this.outputs = Objects.requireNonNull(outputs);
    }

    public Network build() {
        List<Node> nodes = generateIntermediateNodes();

        List<InputNode> inputNodes = inputs.inputs(1).stream().toList();
        List<OutputNode> outputNodes = outputs.outputs(inputNodes.size() + 1).stream().toList();

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
        return nodeSupplier.get(random.nextInt(nodeSupplier.size())).generate(id, capacity);
    }



    public Network copyWithChanceToMutate(Network network) {
        return copyWithChanceToMutate(network, mutationStrategy);
    }

    public Network copyWithChanceToMutate(Network network, List<MutationType> mutationTypes, double mutationRate) {
        return copyWithChanceToMutate(network, MutationType.random(mutationRate, mutationTypes));
    }

    public Network copyWithChanceToMutate(Network network,
                                          List<MutationType> mutationTypes,
                                          double mutationRate,
                                          int maxNumberOfMutations) {
        int numberOfMutations = random.nextInt(maxNumberOfMutations) + 1;
        Network toReturn = network;
        for(int i = 0; i < numberOfMutations; i++){
            toReturn = copyWithChanceToMutate(toReturn, MutationType.random(mutationRate, mutationTypes));
        }
        return toReturn;
    }

    public Network copyWithChanceToMutate(Network network, MutationStrategy mutationStrategy) {
        return copyWithChanceToMutate(network,mutationStrategy.getMutationTypes());
    }

    public Network copyWithChanceToMutate(Network network, List<MutationType> mutationTypes) {
        return copyWithChanceToMutate(network, mutationTypes, mutationRate);
    }

    public Network copyWithChanceToMutate(Network network, final MutationType mutation){
        return copyWithChanceToMutate(network.getIntermediateNodes().stream(),
                network.streamConnections(),
                mutation);
    }
    private Network copyWithChanceToMutate(Stream<Node> intermediateStream,
                                           Stream<Connection> connectionStream,
                                           final MutationType mutation){

        if(mutation == MutationType.REBUILD){
            return build();
        }

        List<InputNode> inputs = this.inputs.inputs(1).stream().toList();
        List<OutputNode> outputs = this.outputs.outputs(inputs.size() + 1).stream().toList();
        List<Node> intermediate = intermediateStream
                .map(x->x.copy())
                .collect(toCollection(ArrayList::new));

        List<Connection> connections = connectionStream
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (mutation == MutationType.NODE_REMOVAL
                && intermediate.size() > minNodes) {
            Node toRemove = randomEntry(intermediate);
            // Only remove node if it won't bring the number of connections bellow the minimum
            if(intermediate.size() - connectionsBelongingTo(toRemove,connections.stream()).count() > minConnection){
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

            connections.add(new Connection(source, destination, randomRange(minBandwith, maxBandwith)
                    ,randomConnectionMultipler()
                    ,Connection.ConnectionType.random()));

        } else if (mutation == MutationType.CONNECTION_REMOVAL && connections.size() > minConnection) {
            connections.remove(random.nextInt(connections.size()));
        } else if (mutation == MutationType.CONNECTION_RANDOMIZE_MULTIPLIER) {
            Connection toModify = connections.get(random.nextInt(connections.size()));
            connections.remove(toModify);
            connections.add(toModify.copyNewMultiplier(randomConnectionMultipler()));
        } else if (mutation == MutationType.ADD_CONNECTION_BANDWIDTH && !connections.isEmpty()) {
            Connection toModify = connections.get(random.nextInt(connections.size()));
            connections.remove(toModify);
            connections.add(toModify.copyModifyBandWidth(bandwidthModificationIncrements));
        } else if (mutation == MutationType.REDUCE_CONNECTION_BANDWIDTH && !connections.isEmpty()) {
            Connection toModify = connections.get(random.nextInt(connections.size()));
            if (toModify.getBandwidth() > bandwidthModificationIncrements) {
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
                            x.getBandwidth(),
                            randomConnectionMultipler(),
                            x.getConnectionType()) )
                    .toList();
        } else if (mutation == MutationType.CONNECTION_TO_NODE
                && intermediate.size() < maxNodes
                && connections.size() < maxConnection
                && connections.size() > 1) {
            Node toAdd = createIntermediateNode(intermediate);
            intermediate.add(toAdd);

            int index = random.nextInt(connections.size());
            Connection toModify = connections.get(random.nextInt(connections.size()));
            connections.remove(index);

            connections.add(new Connection(toModify.getSource(), toAdd,
                    randomRange(minBandwith, maxBandwith),
                    randomConnectionMultipler(),
                    Connection.ConnectionType.random()));
            connections.add(new Connection(toAdd, toModify.getDestination(),
                    randomRange(minBandwith, maxBandwith),
                    randomConnectionMultipler(),
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
                    toModify.getBandwidth(),
                    randomConnectionMultipler(),
                    toModify.getConnectionType()) );
        }

        return new Network(inputs,
                outputs,
                intermediate,
                connections);
    }

    public Stream<Connection> connectionsBelongingTo(Node n, Stream<Connection> connectionStream){
        return connectionStream
                .filter( x -> x.getSource().equals(n) || x.getDestination().equals(n));
    }

    private static List<Node> updateNode(List<Node> nodes, Node toUpdate){
        return Stream.concat(nodes.stream()
                .filter(x->x.getId() != toUpdate.getId()),Stream.of(toUpdate)).toList();
    }

    public enum MutationType {
        NODE_REMOVAL,
        MUTATE_NODE,
        NODE_TYPE,
        NODE_RANDOMIZE_CAPACITY,
        CONNECTION_TO_NODE,
        CONNECTION_ADD,
        CONNECTION_REMOVAL,
        CONNECTION_RANDOMIZE_MULTIPLIER,
        ADD_CONNECTION_BANDWIDTH,
        REDUCE_CONNECTION_BANDWIDTH,
        RANDOMIZE_CONNECTION_BANDWIDTH,
        SHUFFLE_CONNECTION_PRIORITY,
        SHUFFLE_ALL_CONNECTIONS,
        SHUFFLE_CONNECTION,
        REBUILD,
        NONE;
        private static Random random = new Random();

        public static List<MutationType> connectionWeights = List.of(
                ADD_CONNECTION_BANDWIDTH,
                REDUCE_CONNECTION_BANDWIDTH,
                CONNECTION_RANDOMIZE_MULTIPLIER);
        private static final List<MutationType> defaultMutationTypes = List.of(MutationType.values())
                .stream()
                .filter( x -> x != NONE)
                .toList();

        public static MutationType random(double mutationRate) {
            return random(mutationRate, defaultMutationTypes);
        }

        public static MutationType random(double mutationRate, List<MutationType> mutationTypes) {
            if (random.nextDouble() > mutationRate) {
                return NONE;
            }
            return random(mutationTypes);
        }

        public static MutationType random(List<MutationType> mutationTypes) {
            return mutationTypes.get(random.nextInt(mutationTypes.size()));
        }
    }

    private List<Node> generateIntermediateNodes() {
        List<Node> nodes = new ArrayList<>();

        int numberOfNodes = randomRange(minNodes,maxNodes);
        for (int i = 0; i < numberOfNodes; i++) {
            int storage = randomRange(minStorage, maxStorage);

            nodes.add(new MutateableNodeDefault(i + 1000, storage, 10,0));
        }

        return nodes;
    }

    private double randomConnectionMultipler(){
        return random.nextDouble() * 5;
    }

    private static int randomRange(int min, int max) {
        return random.nextInt(min,max+1);
    }

    private List<Connection> generateConnections(Collection<InputNode> inputNodes,
                                                 Collection<OutputNode> outputNodes,
                                                 Collection<Node> intermediateNodes) {
        List<Node> sources = Stream.concat(inputNodes.stream(), intermediateNodes.stream())
                .collect(Collectors.toList());
        List<Node> destinations = Stream.concat(outputNodes.stream(), intermediateNodes.stream())
                .collect(Collectors.toList());

        List<Connection> connections = new ArrayList<>();

        int numberOfConnections = randomRange(minConnection,maxConnection);

        for (int i = 0; i < numberOfConnections; i++) {
            Node source = randomEntry(sources);
            Node destination = randomEntry(destinations);

            connections.add(new Connection(source,
                    destination,
                    randomRange(minBandwith, maxBandwith),
                    randomConnectionMultipler(),
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

    public RandomNetworkBuilder minNodes(int minNodes) {
        if(maxNodes < minNodes){
            throw new IllegalStateException("Min cannot be bigger then max");
        }
        this.minNodes = minNodes;
        return this;
    }

    public RandomNetworkBuilder maxNodes(int maxNodes) {
        if(maxNodes < minNodes){
            throw new IllegalStateException("Max cannot be less then min");
        }
        this.maxNodes = maxNodes;
        return this;
    }

    public RandomNetworkBuilder minConnection(int minConnection) {
        if(maxConnection < minConnection){
            throw new IllegalStateException("Max cannot be less then min");
        }
        this.minConnection = minConnection;
        return this;
    }

    public RandomNetworkBuilder maxConnection(int maxConnection) {
        if(maxConnection < minConnection){
            throw new IllegalStateException("Max cannot be less then min");
        }
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

    public RandomNetworkBuilder mutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
        return this;
    }

    public RandomNetworkBuilder maxActivation(int maxActivation) {
        this.maxActivation = maxActivation;
        return this;
    }

    public RandomNetworkBuilder mutationStrategy(MutationStrategy mutationStrategy){
        this.mutationStrategy = mutationStrategy;
        return this;
    }
}
