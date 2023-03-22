package org.neil.neural.input;

import org.neil.neural.Input;
import org.neil.neural.NodeDefault;

public class YPositionInput extends NodeDefault implements Input {
    public YPositionInput(int capacity) {
        super(10, capacity);
    }

    @Override
    public void input(Inputs toAdd) {
        try {
            clearStorage();
            if (availableCapacity() < toAdd.coordinates.y) {
                addToStorage(availableCapacity());
            } else {
                addToStorage(toAdd.coordinates.y);
            }
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Input copy() {
        return new YPositionInput(getCapacity());
    }
}
