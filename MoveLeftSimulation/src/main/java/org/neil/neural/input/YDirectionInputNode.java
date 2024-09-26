package org.neil.neural.input;

import org.neil.neural.AbstractNode;

public class YDirectionInputNode extends AbstractNode implements InputNode<Inputs> {
    public YDirectionInputNode(int id, int capacity) {
        super(id, capacity, 0);
    }

    @Override
    public void input(Inputs toAdd) {
        clearStorage();
        switch (toAdd.direction.yDirection) {
            case NORTH -> this.addToStorage(this.getCapacity());
            case NEUTRAL -> this.addToStorage(this.getCapacity() >> 1);
            case SOUTH -> this.addToStorage(0);
        }
    }

    @Override
    public InputNode copy() {
        return new YDirectionInputNode(getId(),getCapacity());
    }
}
