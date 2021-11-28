package org.neil.neural.input;

import org.neil.neural.Input;
import org.neil.neural.NodeDefault;

public class XPositionInput extends NodeDefault implements Input {
    public XPositionInput(int capacity) {
        super(6, capacity);
    }

    @Override
    public void input(Inputs toAdd) {
        clearStorage();
        if(getCapacity() < toAdd.coordinates.x){
            addToStorage(getCapacity());
        }else{
            addToStorage(toAdd.coordinates.x);
        }
    }

    @Override
    public Input copy() {
        return new XPositionInput(getCapacity());
    }
}
