package org.neil.neural.output;

import org.neil.neural.AbstractNode;
import org.neil.object.Creature;

public class RightDirectionOutputNode extends AbstractNode implements OutputNode<Creature> {

    public RightDirectionOutputNode(int id, int capacity) {
        super(id, capacity, 0,0);
    }

    @Override
    public void consumeOutput(Creature creature) {
        if (this.getCapacity() == this.getStored()) {
            creature.setDirection(creature.getDirection().right());
        }
        this.clearStorage();
    }

    @Override
    public OutputNode copy(){
        return new RightDirectionOutputNode(getId(),getCapacity());
    }
}
