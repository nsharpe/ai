package org.neil.neural.input;

import org.neil.neural.NodeDefault;

import java.util.Random;


/**
 * An input node that will be filled randomly from zero to capacity inclusive
 *
 * @Author Neil Sharpe
 *
 */
public class RandomInputNode<E> extends NodeDefault implements InputNode<E> {
    private static Random random = new Random();
    public RandomInputNode(int id, int capacity) {
        super(id, capacity, 1);
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
