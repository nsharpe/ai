package org.neil.neural.input;

import org.neil.neural.NodeDefault;

public class ProximityInputNode extends NodeDefault implements InputNode<Inputs> {
    public ProximityInputNode(int id, int capacity) {
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
    public InputNode copy() {
        return new ProximityInputNode(getId(), getCapacity());
    }
}
