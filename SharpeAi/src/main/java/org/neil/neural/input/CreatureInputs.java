package org.neil.neural.input;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreatureInputs implements InputNodeGenerator {
    private final int defaultCapacity = 256;

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
                new YPositionInputNode(startingIndex++, defaultCapacity)
        ).collect(Collectors.toList());
    }
}
