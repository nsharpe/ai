package org.neil.neural.output;

import org.neil.neural.AbstractNode;
import org.neil.object.Creature;

public class LeftDirectionOutputNode extends AbstractNode implements OutputNode<Creature> {

    public LeftDirectionOutputNode(int id, int capacity) {
        super(id, capacity, 0,0);
    }

    @Override
    public void consumeOutput(Creature creature) {
        if (this.getCapacity() == this.getStored()) {
            creature.setDirection(creature.getDirection().left());
        }
        this.clearStorage();
    }

    @Override
    public OutputNode copy() {
        return new LeftDirectionOutputNode(getId(), getCapacity());
    }
}
