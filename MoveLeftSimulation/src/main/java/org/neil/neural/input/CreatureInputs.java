package org.neil.neural.input;

import org.neil.board.Coordinates;

import java.io.Serial;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreatureInputs implements InputNodeGenerator {

    @Serial
    private static final long serialVersionUID= 4149427688655578599L;

    private final int defaultCapacity = Integer.MAX_VALUE;
    private Supplier<Coordinates> coordinatesSupplier = null;

    public CreatureInputs(Supplier<Coordinates> coordinatesSupplier) {
        this.coordinatesSupplier = coordinatesSupplier;
    }

    public CreatureInputs() {
        // noop
    }


    @Override
    public Collection<InputNode> inputs(int startingIndex) {

        if(coordinatesSupplier == null){
            return defaultInputs(startingIndex);
        }

        Collection<InputNode> toReturn = defaultInputs(startingIndex);

        return Stream.concat(
                toReturn.stream(),
                destinationInputs(toReturn.size() + startingIndex)
        ).toList();
    }

    public Collection<InputNode> defaultInputs(int startingIndex) {
        return Stream.of(
                new ConstantInputNode(startingIndex++, defaultCapacity),
                new DirectionViewInputNode(startingIndex++, defaultCapacity),
                new MovementBlockedInputNode(startingIndex++, defaultCapacity),
                new ProximityInputNode(startingIndex++, defaultCapacity),
                new RandomInputNode(startingIndex++, defaultCapacity),
                new XDirectionInputNode(startingIndex++, defaultCapacity),
                new XPositionInputNode(startingIndex++, defaultCapacity),
                new YDirectionInputNode(startingIndex++,defaultCapacity),
                new YPositionInputNode(startingIndex++, defaultCapacity)
        ).collect(Collectors.toList());
    }

    public Stream<InputNode> destinationInputs(int startingIndex){
        return Stream.of(
                new DistanceInputNode(startingIndex++, defaultCapacity, coordinatesSupplier),
                new XDestinationInputNode(startingIndex++, defaultCapacity,coordinatesSupplier),
                new YDestinationInputNode(startingIndex++, defaultCapacity,coordinatesSupplier)
        );
    }


}
