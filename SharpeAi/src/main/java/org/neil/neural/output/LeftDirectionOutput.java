package org.neil.neural.output;

import org.neil.neural.NodeDefault;
import org.neil.neural.Output;
import org.neil.object.Creature;

public class LeftDirectionOutput extends NodeDefault implements Output {

    public LeftDirectionOutput(int id, int capacity) {
        super(id, capacity);
    }

    @Override
    public void consumeOutput(Creature creature) {
        if (this.getCapacity() == this.getStored()) {
            creature.setDirection(creature.getDirection().left());
        }
        this.clearStorage();
    }

    @Override
    public Output copy() {
        return new LeftDirectionOutput(getId(), getCapacity());
    }
}
