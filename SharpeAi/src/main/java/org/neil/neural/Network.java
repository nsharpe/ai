package org.neil.neural;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.neil.neural.input.InputNode;
import org.neil.neural.output.OutputNode;
import org.neil.neural.serializer.NetworkDeserializer;
import org.neil.neural.serializer.NetworkSerializer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonSerialize(using = NetworkSerializer.class)
@JsonDeserialize(using = NetworkDeserializer.class)
public class Network {

    private Map<Node, List<Connection>> connections;
    private final List<InputNode> inputNodes;
    private final List<OutputNode> outputNodes;
    private final List<? extends Node> intermediate;

    @JsonCreator
    public Network(@JsonProperty("inputs") List<InputNode> inputNodes,
                   @JsonProperty("outputs")  List<OutputNode> outputNodes,
                   @JsonProperty("intermediate")  List<? extends Node> intermediate,
                   @JsonProperty("connections") List<Connection> connections) {
        this.inputNodes = Objects.requireNonNull(inputNodes)
                .stream()
                .toList();
        this.intermediate = Objects.requireNonNull(intermediate)
                .stream()
                .toList();
        this.outputNodes = Objects.requireNonNull(outputNodes)
                .stream()
                .toList();;
        Objects.requireNonNull(connections);

        Map<Integer,Node> nodeId = Stream.concat(
                inputNodes.stream(),Stream.concat(
                outputNodes.stream(),
                intermediate.stream())
        ).collect(Collectors.toMap(x->x.getId(), x->x));

        List<Connection> updatedConnections = connections.stream()
                .map(x-> new Connection(nodeId.get(x.getSource().getId()),
                        nodeId.get(x.getDestination().getId()),
                                x.getBandwidth(),
                                x.getMultiplier(),
                                x.getConnectionType()
                        )).toList();

        this.connections = new HashMap<>();

        for (Connection connection : updatedConnections) {
            this.connections.computeIfAbsent(connection.getSource(), x -> new ArrayList<>())
                    .add(connection);
        }
    }
    private Network(NetworkBuilder networkBuilder) {
        this(networkBuilder.inputNodes,
                networkBuilder.outputNodes,
                networkBuilder.intermediate,
                networkBuilder.connections);
    }

    public void increment() {
        try {
            connections.values()
                    .stream()
                    .flatMap(x -> x.stream())
                    .forEach(x -> x.activate());
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public List<InputNode> getInputs() {
        return Collections.unmodifiableList(inputNodes);
    }

    public List<OutputNode> getOutputs() {
        return Collections.unmodifiableList(outputNodes);
    }

    public List<Node> getIntermediateNodes(){
        return Collections.unmodifiableList(intermediate);
    }

    public Stream<Connection> streamConnections(){
        return connections.values().stream().flatMap(x-> x.stream());
    }

    public static NetworkBuilder builder(){
        return new NetworkBuilder();
    }

    public static class NetworkBuilder{
        private List<InputNode> inputNodes;
        private List<OutputNode> outputNodes;
        private List<Node> intermediate;
        private List<Connection> connections = new ArrayList<>();

        private NetworkBuilder(){
            //noop
        }

        public NetworkBuilder input(List<InputNode> nodes) {
            this.inputNodes = nodes;
            return this;
        }

        public NetworkBuilder output(List<OutputNode> nodes) {
            this.outputNodes = nodes;
            return this;
        }

        public NetworkBuilder intermediate(List<Node> nodes) {
            this.intermediate = nodes;
            return this;
        }

        public NetworkBuilder addConnections(List<Connection> connections) {
            this.connections.addAll(connections);
            return this;
        }

        public Network build(){
            return new Network(this);
        }
    }
}
