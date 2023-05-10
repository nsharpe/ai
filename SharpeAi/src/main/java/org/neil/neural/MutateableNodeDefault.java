package org.neil.neural;

public class MutateableNodeDefault extends NodeDefault implements MutateableNode{

    public MutateableNodeDefault(int id, int capacity, int activationLimit) {
        super(id, capacity, activationLimit);
    }

    @Override
    public MutateableNode mutate(int capacityMin, int capacityMax, int activationMax) {
        int capacity = generateNewCapacity(capacityMin,capacityMax);
        int activation = generateNewActivation(capacity,activationMax);
        return new MutateableNodeDefault(this.getId(),
                capacity,
                activation);
    }

    @Override
    public Node copy() {
        return new MutateableNodeDefault(this.getId(),this.getCapacity(),this.getActivationLimit());
    }
}
