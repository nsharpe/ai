package org.neil.neural;

public class NodeCapacityPastThreshold extends NodeDefault {
    private final int threshHold;

    public NodeCapacityPastThreshold(int id, int threshHold) {
        super(id);
        this.threshHold = threshHold;
    }

    public NodeCapacityPastThreshold(int id, int capacity, int threshHold) {
        super(id, capacity);
        this.threshHold = threshHold;
    }

    public NodeCapacityPastThreshold(NodeCapacityPastThreshold node) {
        super(node);
        this.threshHold = node.threshHold;
    }

    @Override
    public void addToStorage(int toAdd) {
        if (toAdd + super.availableCapacity() > super.getCapacity()) {
            super.fillStorage();
        } else if (toAdd - super.availableCapacity() < 0) {
            super.clearStorage();
        } else {
            super.addToStorage(toAdd);
        }
    }

    @Override
    public int availableCapacity() {
        return super.getCapacity() >= threshHold ? threshHold : 0;
    }

    @Override
    public Node copy() {
        return new NodeCapacityPastThreshold(this);
    }
}
