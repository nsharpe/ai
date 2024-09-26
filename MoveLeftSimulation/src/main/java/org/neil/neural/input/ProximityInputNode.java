package org.neil.neural.input;

import org.neil.neural.AbstractNode;

public class ProximityInputNode extends AbstractNode implements InputNode<Inputs> {
    public ProximityInputNode(int id, int capacity) {
        super(id, capacity, 0);
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
