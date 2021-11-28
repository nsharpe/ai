package org.neil.neural.input;

import org.neil.neural.Input;
import org.neil.neural.NodeDefault;

public class XPositionInput extends NodeDefault implements Input {
    public XPositionInput(int capacity) {
        super(6, capacity);
    }

    @Override
    public void input(Inputs toAdd) {
        try {
            clearStorage();
            if (availableCapacity() < toAdd.coordinates.x) {
                addToStorage(availableCapacity());
            } else {
                addToStorage(toAdd.coordinates.x);
            }
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Input copy() {
        return new XPositionInput(getCapacity());
    }
}
