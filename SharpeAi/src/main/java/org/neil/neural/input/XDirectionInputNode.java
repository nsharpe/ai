package org.neil.neural.input;

import org.neil.neural.NodeDefault;

public class XDirectionInputNode extends NodeDefault implements InputNode<Inputs> {
    public XDirectionInputNode(int id, int capacity) {
        super(id, capacity);
    }

    @Override
    public void input(Inputs toAdd) {
        clearStorage();
        switch (toAdd.direction.xDirection) {
            case EAST -> this.addToStorage(this.getCapacity());
            case NEUTRAL -> this.addToStorage(this.getCapacity() >> 1);
            case WEST -> this.addToStorage(0);
        }
    }

    @Override
    public InputNode copy() {
        return new XDirectionInputNode(getId(),getCapacity());
    }
}
