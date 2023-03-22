package org.neil.neural;

public class NodeAlwaysFull extends NodeDefault{

    public NodeAlwaysFull(int id) {
        super(id);
    }

    public NodeAlwaysFull(int id, int capacity) {
        super(id, capacity);
    }

    public NodeAlwaysFull(Node node) {
        super(node);
    }

    @Override
    public void addToStorage(int toAdd) {
        // noop
    }

    @Override
    public int availableCapacity() {
        return getCapacity();
    }

    @Override
    public Node copy() {
        return new NodeAlwaysFull(this);
    }
}
