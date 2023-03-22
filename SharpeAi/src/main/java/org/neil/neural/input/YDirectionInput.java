package org.neil.neural.input;

import org.neil.neural.Input;
import org.neil.neural.NodeDefault;

public class YDirectionInput extends NodeDefault implements Input {
    public YDirectionInput(int capacity) {
        super(5, capacity);
    }

    @Override
    public void input(Inputs toAdd) {
        clearStorage();
        switch (toAdd.direction.yDirection) {
            case NORTH -> this.addToStorage(this.getCapacity());
            case NEUTRAL -> this.addToStorage(this.getCapacity() >> 1);
            case SOUTH -> this.addToStorage(0);
        }
    }

    @Override
    public Input copy() {
        return new YDirectionInput(getCapacity());
    }
}
