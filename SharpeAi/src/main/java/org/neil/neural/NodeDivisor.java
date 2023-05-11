package org.neil.neural;

public class NodeDivisor extends NodeDefault implements MutateableNode{

    public NodeDivisor(int id) {
        super(id);
    }

    public NodeDivisor(int id, int capacity, int activationLimit) {
        super(id, capacity, activationLimit);
    }

    public NodeDivisor(Node node) {
        super(node);
    }

    @Override
    public void addToStorage(int toAdd) {
        if(toAdd > 0) {
            toAdd = toAdd >> 1;
        }
        if(toAdd > getCapacity() - getStored()){
            toAdd = getCapacity() - getStored();
        }
        super.addToStorage(toAdd);
    }

    @Override
    public MutateableNode mutate(int capacityMin, int capacityMax, int activationMax) {
        int capacity = generateNewCapacity(capacityMin,capacityMax);
        return new NodeDivisor(this.getId(),capacity,generateNewActivation(capacity,activationMax));
    }

    @Override
    public Node copy() {
        return new NodeDivisor(this);
    }
}
