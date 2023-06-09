package org.neil.neural.input;

import org.neil.neural.NodeDefault;

import java.util.Random;

/**
 * An input node that at the start of a tick for a neural network fills to capacity
 *
 * @Author Neil Sharpe
 *
 */
public class ConstantInputNode<E> extends NodeDefault implements InputNode<E> {
    private static Random random = new Random();

    public ConstantInputNode(int id, int capacity) {
        super(id, capacity, capacity / 2);
    }

    @Override
    public void input(E toAdd) {
        try {
            clearStorage();
            addToStorage(availableCapacity());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int availableCapacity() {
        return getCapacity();
    }

    @Override
    public void addToStorage(int toAdd) {
        fillStorage();
    }

    @Override
    public InputNode copy() {
        return new ConstantInputNode(getId(), getCapacity());
    }
}
