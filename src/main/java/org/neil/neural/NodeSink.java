package org.neil.neural;

public class NodeSink extends NodeDefault{

    public NodeSink(int id) {
        super(id, 1);
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
