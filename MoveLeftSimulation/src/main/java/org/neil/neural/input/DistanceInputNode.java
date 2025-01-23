package org.neil.neural.input;

import org.neil.board.Coordinates;
import org.neil.neural.AbstractNode;

import java.util.function.Supplier;

public class DistanceInputNode extends AbstractNode implements InputNode<Inputs> {
    private final Supplier<Coordinates> coordinatesSupplier;
    public DistanceInputNode(int id, int capacity, Supplier<Coordinates> coordinatesSupplier) {
        super(id, capacity, 0,0);
        this.coordinatesSupplier = coordinatesSupplier;
    }

    @Override
    public void input(Inputs toAdd) {
        clearStorage();
        this.addToStorage((int)coordinatesSupplier.get().distance(toAdd.coordinates));
    }

    @Override
    public InputNode copy() {
        return new DistanceInputNode(getId(),getCapacity(), coordinatesSupplier);
    }
}