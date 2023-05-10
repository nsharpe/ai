package org.neil.neural;

public class NodeMultiplier extends NodeDefault{

    public NodeMultiplier(int id) {
        super(id);
    }

    public NodeMultiplier(int id, int capacity) {
        super(id, capacity, capacity / 2);
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
}
