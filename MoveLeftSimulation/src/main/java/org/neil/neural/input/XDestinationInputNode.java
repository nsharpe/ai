package org.neil.neural.input;

import org.neil.board.Coordinates;
import org.neil.neural.NodeDefault;

import java.util.function.Supplier;

public class XDestinationInputNode extends NodeDefault implements InputNode<Inputs> {
    private final Supplier<Coordinates> coordinatesSupplier;
    public XDestinationInputNode(int id, int capacity, Supplier<Coordinates> coordinatesSupplier) {
        super(id, capacity);
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
