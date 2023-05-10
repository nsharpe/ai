package org.neil.neural.input;

import org.neil.neural.NodeDefault;

public class MovementBlockedInputNode extends NodeDefault implements InputNode<Inputs> {
    public MovementBlockedInputNode(int id, int capacity) {
        super(id, capacity, 0);
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
    public InputNode copy() {
        return new MovementBlockedInputNode(getId(), getCapacity());
    }
}
