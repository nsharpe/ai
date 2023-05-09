package org.neil.neural.input;

import org.neil.board.Coordinates;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreatureInputs implements InputNodeGenerator {
    private final int defaultCapacity = 256;
    Supplier<Coordinates> coordinatesSupplier;

    public CreatureInputs(Supplier<Coordinates> coordinatesSupplier) {
        this.coordinatesSupplier = coordinatesSupplier;
    }

    @Override
    public Collection<InputNode> inputs(int startingIndex) {
        return Stream.of(
                new ConstantInputNode(startingIndex++, defaultCapacity),
                new DirectionViewInputNode(startingIndex++, defaultCapacity),
                new MovementBlockedInputNode(startingIndex++, defaultCapacity),
                new ProximityInputNode(startingIndex++, defaultCapacity),
                new RandomInputNode(startingIndex++, defaultCapacity),
                new XDirectionInputNode(startingIndex++, defaultCapacity),
                new XPositionInputNode(startingIndex++, defaultCapacity),
                new YDirectionInputNode(startingIndex++,defaultCapacity),
                new YPositionInputNode(startingIndex++, defaultCapacity),
                new XDestinationInputNode(startingIndex++, defaultCapacity,coordinatesSupplier),
                new YDestinationInputNode(startingIndex++, defaultCapacity,coordinatesSupplier)
        ).collect(Collectors.toList());
    }
}
