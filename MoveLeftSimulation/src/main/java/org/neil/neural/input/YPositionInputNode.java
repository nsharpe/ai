package org.neil.neural.input;

import org.neil.neural.AbstractNode;

public class YPositionInputNode extends AbstractNode implements InputNode<Inputs> {
    public YPositionInputNode(int id, int capacity) {
        super(id, capacity, 0);
    }

    @Override
    public void input(Inputs toAdd) {
        try {
            clearStorage();
            if (availableCapacity() < toAdd.coordinates.y) {
                addToStorage(availableCapacity());
            } else {
                addToStorage(toAdd.coordinates.y);
            }
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public InputNode copy() {
        return new YPositionInputNode(getId(),getCapacity());
    }
}
