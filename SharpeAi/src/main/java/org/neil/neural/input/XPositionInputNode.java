package org.neil.neural.input;

import org.neil.neural.NodeDefault;

public class XPositionInputNode extends NodeDefault implements InputNode {
    public XPositionInputNode(int id, int capacity) {
        super(id, capacity);
    }

    @Override
    public void input(Inputs toAdd) {
        try {
            clearStorage();
            if (availableCapacity() < toAdd.coordinates.x) {
                addToStorage(availableCapacity());
            } else {
                addToStorage(toAdd.coordinates.x);
            }
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public InputNode copy() {
        return new XPositionInputNode(getId(), getCapacity());
    }
}
