package org.neil.neural;

public class NodeAlwaysFull extends NodeDefault{

    public NodeAlwaysFull(int id) {
        super(id);
    }

    public NodeAlwaysFull(int id, int capacity) {
        super(id, capacity, capacity / 2);
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
        return 0;
    }

    @Override
    public Node copy() {
        return new NodeAlwaysFull(this);
    }
}
