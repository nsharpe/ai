package org.neil.neural.output;

import org.neil.board.Coordinates;
import org.neil.neural.AbstractNode;
import org.neil.object.Creature;

public class MoveOutputNode extends AbstractNode implements OutputNode<Creature> {

    public MoveOutputNode(int id, int capacity) {
        super(id, capacity, 0);
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
