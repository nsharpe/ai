package org.neil.neural;

public class NodeSink extends NodeDefault{

    public NodeSink(int id) {
        super(id, Integer.MAX_VALUE - 1, Integer.MAX_VALUE);
    }

    public NodeSink(Node node) {
        super(node);
    }

    @Override
    public void addToStorage(int toAdd) {
        // do nothing
    }

    @Override
    public Node copy() {
        return new NodeSink(this);
    }
}
