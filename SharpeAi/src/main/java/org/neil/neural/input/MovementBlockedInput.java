package org.neil.neural.input;

import org.neil.neural.Input;
import org.neil.neural.NodeDefault;

public class MovementBlockedInput extends NodeDefault implements Input {
    public MovementBlockedInput(int id, int capacity) {
        super(id, capacity);
    }

    @Override
    public void input(Inputs inputs) {
        clearStorage();

        if(!inputs.coordinateMap.isEmpty(inputs.direction.position(inputs.coordinates)))
        {
            addToStorage(availableCapacity());
        }
    }

    @Override
    public Input copy() {
        return new MovementBlockedInput(getId(), getCapacity());
    }
}
