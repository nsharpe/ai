package org.neil.neural;

import org.neil.neural.input.InputNode;
import org.neil.neural.output.OutputNode;

import java.util.*;
import java.util.stream.Stream;

public class Network {

    private Map<Node, List<Connection>> connections;
    private final List<InputNode> inputNodes;
    private final List<OutputNode> outputNodes;
    private final List<Node> intermediate;

    public Network(List<Node> nodes,
                   List<Connection> connections) {

        Objects.requireNonNull(nodes);
        Objects.requireNonNull(connections);

        this.connections = new HashMap<>();

        for (Connection connection : connections) {
            this.connections.computeIfAbsent(connection.getSource(), x -> new ArrayList<>())
                    .add(connection);
        }

        inputNodes = new ArrayList<>();
        outputNodes = new ArrayList<>();
        intermediate = new ArrayList<>();

        for (Node node : nodes) {
            if (node instanceof InputNode) {
                inputNodes.add((InputNode) node);
            } else if (node instanceof OutputNode) {
                outputNodes.add((OutputNode) node);
            } else {
                intermediate.add( node );
            }
        }
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
}
