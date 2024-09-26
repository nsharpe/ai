package org.neil.neural;

import java.io.Serial;

public class MutateableNodeDefault extends AbstractNode implements MutateableNode{
    @Serial
    private static final long serialVersionUID = 3108793121583625752L;

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

    public static Mutator mutator(){
        return new Mutator();
    }

    public static class Mutator implements NodeMutator<MutateableNodeDefault>{

        @Serial
        private static final long serialVersionUID = 4408047468173402541L;

        @Override
        public MutateableNodeDefault generate(int id, int capacity) {
            return new MutateableNodeDefault(id,capacity,capacity/2);
        }
    }
}
