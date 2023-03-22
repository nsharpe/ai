package org.neil.neural.input;

import org.neil.neural.Input;
import org.neil.neural.NodeDefault;

public class XDirectionInput extends NodeDefault implements Input {
    public XDirectionInput(int capacity) {
        super(4, capacity);
    }

    @Override
    public void input(Inputs toAdd) {
        clearStorage();
        switch (toAdd.direction.xDirection) {
            case EAST -> this.addToStorage(this.getCapacity());
            case NEUTRAL -> this.addToStorage(this.getCapacity() >> 1);
            case WEST -> this.addToStorage(0);
        }
    }

    @Override
    public Input copy() {
        return new XDirectionInput(getCapacity());
    }
}
