package org.neil.neural;

public class NodeMultiplier extends NodeDefault implements MutateableNode{

    public NodeMultiplier(int id) {
        super(id);
    }

    public NodeMultiplier(int id, int capacity, int activation) {
        super(id, capacity, activation);
    }

    public NodeMultiplier(Node node) {
        super(node);
    }

    @Override
    public void addToStorage(int toAdd) {
        if(toAdd > 0) {
            toAdd = toAdd << 1;
        }
        if(toAdd > getCapacity() - getStored()){
            toAdd = getCapacity() - getStored();
        }
        super.addToStorage(toAdd);
    }

    @Override
    public Node copy() {
        return new NodeMultiplier(this);
    }

    @Override
    public MutateableNode mutate(int capacityMin, int capacityMax, int activationMax) {
        int capacity = generateNewCapacity(capacityMin,capacityMax);
        return new NodeMultiplier(this.getId(),capacity,generateNewActivation(capacity,activationMax));
    }
}
