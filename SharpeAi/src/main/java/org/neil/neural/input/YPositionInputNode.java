package org.neil.neural.input;

import org.neil.neural.NodeDefault;

public class YPositionInputNode extends NodeDefault implements InputNode {
    public YPositionInputNode(int id, int capacity) {
        super(id, capacity);
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
