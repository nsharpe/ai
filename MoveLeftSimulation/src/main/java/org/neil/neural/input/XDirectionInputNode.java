package org.neil.neural.input;

import org.neil.neural.AbstractNode;

public class XDirectionInputNode extends AbstractNode implements InputNode<Inputs> {
    public XDirectionInputNode(int id, int capacity) {
        super(id, capacity, 0);
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
