package org.neil.neural.input;

import org.neil.neural.NodeDefault;

import java.util.Random;

public class RandomInputNode<E> extends NodeDefault implements InputNode<E> {
    private static Random random = new Random();
    public RandomInputNode(int id, int capacity) {
        super(id, capacity);
    }

    @Override
    public void input(E toAdd) {
        try {
            clearStorage();
            addToStorage(random.nextInt(availableCapacity()));
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public InputNode copy() {
        return new RandomInputNode(getId(), getCapacity());
    }
}
