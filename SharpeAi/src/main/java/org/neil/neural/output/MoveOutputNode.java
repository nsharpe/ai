package org.neil.neural.output;

import org.neil.map.Coordinates;
import org.neil.neural.NodeDefault;
import org.neil.object.Creature;

public class MoveOutputNode extends NodeDefault implements OutputNode {

    public MoveOutputNode(int id, int capacity) {
        super(id, capacity);
    }

    @Override
    public void consumeOutput(Creature creature) {
        if (this.getCapacity() == this.getStored()) {
            Coordinates newPosition = creature.getDirection().position(creature.getPosition());

            creature.getCoordinateMap()
                    .move(creature, newPosition);
        }

        this.clearStorage();
    }

    @Override
    public OutputNode copy(){
        return new MoveOutputNode(getId(), getCapacity());
    }
}
