package org.neil.neural.input;

import org.neil.board.Coordinates;
import org.neil.neural.AbstractNode;

import java.util.function.Supplier;

public class XDestinationInputNode extends AbstractNode implements InputNode<Inputs> {
    private final Supplier<Coordinates> coordinatesSupplier;
    public XDestinationInputNode(int id, int capacity, Supplier<Coordinates> coordinatesSupplier) {
        super(id, capacity, 0,0);
        this.coordinatesSupplier = coordinatesSupplier;
    }

    @Override
    public void input(Inputs toAdd) {
        clearStorage();
        this.addToStorage(coordinatesSupplier.get().x);
    }

    @Override
    public InputNode copy() {
        return new XDestinationInputNode(getId(),getCapacity(),coordinatesSupplier);
    }
}
