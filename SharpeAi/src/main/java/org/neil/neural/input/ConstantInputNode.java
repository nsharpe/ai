package org.neil.neural.input;

import org.neil.neural.NodeDefault;

import java.util.Random;

public class ConstantInputNode extends NodeDefault implements InputNode {
    private static Random random = new Random();

    public ConstantInputNode(int id, int capacity) {
        super(id, capacity);
    }

    @Override
    public void input(Inputs toAdd) {
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
