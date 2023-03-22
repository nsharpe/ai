package org.neil.neural.input;

import org.neil.neural.Input;
import org.neil.neural.NodeDefault;

public class ProximityInput extends NodeDefault implements Input {
    public ProximityInput(int id, int capacity) {
        super(id, capacity);
    }

    @Override
    public void input(Inputs inputs) {
        clearStorage();

        if (inputs.coordinates.adjacent()
                .stream().anyMatch(x -> !inputs.coordinateMap.isEmpty(x))) {
            fillStorage();
        }
    }

    @Override
    public Input copy() {
        return new ProximityInput(getId(), getCapacity());
    }
}
