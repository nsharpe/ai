package org.neil.neural.input;

import org.neil.neural.Input;
import org.neil.neural.NodeDefault;

import java.util.Random;

public class RandomInput extends NodeDefault implements Input {
    private static Random random = new Random();
    public RandomInput(int capacity) {
        super(11, capacity);
    }

    @Override
    public void input(Inputs toAdd) {
        try {
            clearStorage();
            addToStorage(random.nextInt(availableCapacity()));
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Input copy() {
        return new RandomInput(getCapacity());
    }
}
