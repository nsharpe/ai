package org.neil.neural.input;

import org.neil.neural.Input;
import org.neil.neural.NodeDefault;

import java.util.Random;

public class ConstantInput extends NodeDefault implements Input {
    private static Random random = new Random();
    public ConstantInput(int capacity) {
        super(12, capacity);
    }

    @Override
    public void input(Inputs toAdd) {
        try {
            clearStorage();
            addToStorage(availableCapacity());
        }catch (Exception e){
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
    public Input copy() {
        return new ConstantInput(getCapacity());
    }
}
