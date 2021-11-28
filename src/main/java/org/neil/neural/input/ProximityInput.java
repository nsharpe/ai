package org.neil.neural.input;

import org.neil.neural.Input;
import org.neil.neural.NodeDefault;

public class ProximityInput extends NodeDefault implements Input {
    public ProximityInput(int capacity) {
        super(7, capacity);
    }

    @Override
    public void input(Inputs inputs) {
        clearStorage();

        if (inputs.coordinates.adjacent()
                .stream().anyMatch(x -> !inputs.coordinateMap.isEmpty(x))) {
            addToStorage(availableCapacity());
        }
    }

    @Override
    public Input copy() {
        return new ProximityInput(getCapacity());
    }
}
