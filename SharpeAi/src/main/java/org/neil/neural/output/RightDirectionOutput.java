package org.neil.neural.output;

import org.neil.neural.NodeDefault;
import org.neil.neural.Output;
import org.neil.object.Creature;

public class RightDirectionOutput extends NodeDefault implements Output {

    public RightDirectionOutput(int capacity) {
        super(2, capacity);
    }

    @Override
    public void consumeOutput(Creature creature) {
        if (this.getCapacity() == this.getStored()) {
            creature.setDirection(creature.getDirection().right());
        }
        this.clearStorage();
    }

    @Override
    public Output copy(){
        return new RightDirectionOutput(getCapacity());
    }
}
