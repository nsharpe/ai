package org.neil.neural;

public class NodeDivisor extends NodeDefault{

    public NodeDivisor(int id) {
        super(id);
    }

    public NodeDivisor(int id, int capacity) {
        super(id, capacity);
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
    public Node copy() {
        return new NodeDivisor(this);
    }
}
