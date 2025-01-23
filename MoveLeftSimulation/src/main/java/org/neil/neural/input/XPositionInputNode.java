package org.neil.neural.input;

import org.neil.neural.AbstractNode;

public class XPositionInputNode extends AbstractNode implements InputNode<Inputs> {
    public XPositionInputNode(int id, int capacity) {
        super(id, capacity, 0,0);
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
