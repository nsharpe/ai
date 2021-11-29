package org.neil.neural;

import java.util.*;
import java.util.stream.Stream;

public class Network {

    private Map<Node, List<Connection>> connections;
    private final List<Input> inputs;
    private final List<Output> outputs;
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

        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
        intermediate = new ArrayList<>();

        for (Node node : nodes) {
            if (node instanceof Input) {
                inputs.add((Input) node);
            } else if (node instanceof Output) {
                outputs.add((Output) node);
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

    public List<Input> getInputs() {
        return Collections.unmodifiableList(inputs);
    }

    public List<Output> getOutputs() {
        return Collections.unmodifiableList(outputs);
    }

    public List<Node> getIntermediateNodes(){
        return Collections.unmodifiableList(intermediate);
    }

    public Stream<Connection> streamConnections(){
        return connections.values().stream().flatMap(x-> x.stream());
    }
}
