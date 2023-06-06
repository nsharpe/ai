package org.neil.neural;

import java.util.ArrayList;
import java.util.List;

public class NodeMax extends NodeDefault implements MutateableNode{

    private int numberOfInputsKept = 5;
    private List<Integer> lastInputs = new ArrayList<>();

    private volatile Integer currentMax = 0;

    public NodeMax(int id) {
        super(id);
    }

    public NodeMax(int id, int capacity, int activation) {
        super(id, capacity, activation);
    }

    public NodeMax(Node node) {
        super(node);
    }

    @Override
    public void addToStorage(int toAdd) {
        if(toAdd > getCapacity()){
            toAdd = getCapacity();
        }
        if(lastInputs.size() > numberOfInputsKept){
            lastInputs.remove(0);
        }
        lastInputs.add(toAdd);

        setCurrentMax();
    }

    private void setCurrentMax(){
        currentMax = lastInputs.stream()
                .mapToInt(x->x)
                .filter( x -> x > 0)
                .max()
                .orElse(0);
    }

    @Override
    public boolean isActivateable() {
        return currentMax > getActivationLimit();
    }

    @Override
    public int getStored() {
        return currentMax;
    }

    @Override
    public Node copy() {
        return new NodeMax(this);
    }

    @Override
    public MutateableNode mutate(int capacityMin, int capacityMax, int activationMax) {
        int capacity = generateNewCapacity(capacityMin,capacityMax);
        return new NodeMax(this.getId(),capacity,generateNewActivation(capacity,activationMax));
    }
}
