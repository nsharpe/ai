package org.neil.neural;

public class NodeAlwaysEmpty extends NodeDefault{

    public NodeAlwaysEmpty(int id) {
        super(id);
    }

    public NodeAlwaysEmpty(int id, int capacity) {
        super(id, capacity, capacity / 2);
    }

    public NodeAlwaysEmpty(Node node) {
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
        return new NodeAlwaysEmpty(this);
    }
}
