package org.neil.neural.input;

import org.neil.board.Coordinates;
import org.neil.neural.AbstractNode;
import org.neil.object.Direction;

public class MovementBlockedInputNode extends AbstractNode implements InputNode<Inputs> {
    public MovementBlockedInputNode(int id, int capacity) {
        super(id, capacity, 0);
    }

    @Override
    public void input(Inputs inputs) {
        clearStorage();

        Direction direction = inputs.direction;
        Coordinates currentLocation = inputs.coordinates;

        if(!inputs.coordinateMap.isEmpty(direction.position(currentLocation)))
        {
            addToStorage(availableCapacity());
        }
    }

    @Override
    public InputNode copy() {
        return new MovementBlockedInputNode(getId(), getCapacity());
    }
}
