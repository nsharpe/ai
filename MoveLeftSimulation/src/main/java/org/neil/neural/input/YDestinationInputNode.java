package org.neil.neural.input;

import org.neil.board.Coordinates;
import org.neil.neural.NodeDefault;

import java.util.function.Supplier;

public class YDestinationInputNode extends NodeDefault implements InputNode<Inputs> {
    private final Supplier<Coordinates> coordinatesSupplier;
    public YDestinationInputNode(int id, int capacity, Supplier<Coordinates> coordinatesSupplier) {
        super(id, capacity, 0);
        this.coordinatesSupplier = coordinatesSupplier;
    }

    @Override
    public void input(Inputs toAdd) {
        clearStorage();
        this.addToStorage(coordinatesSupplier.get().y);
    }

    @Override
    public InputNode copy() {
        return new YDestinationInputNode(getId(),getCapacity(), coordinatesSupplier);
    }
}
