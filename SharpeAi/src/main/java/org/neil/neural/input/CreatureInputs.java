package org.neil.neural.input;

import org.neil.neural.Input;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreatureInputs implements InputNodes {
    private final int defaultCapacity = 256;

    @Override
    public Collection<Input> inputs(int startingIndex) {
        return Stream.of(
                new ConstantInput(startingIndex++, defaultCapacity),
                new DirectionViewInput(startingIndex++, defaultCapacity),
                new MovementBlockedInput(startingIndex++, defaultCapacity),
                new ProximityInput(startingIndex++, defaultCapacity),
                new RandomInput(startingIndex++, defaultCapacity),
                new XDirectionInput(startingIndex++, defaultCapacity),
                new XPositionInput(startingIndex++, defaultCapacity),
                new YDirectionInput(startingIndex++,defaultCapacity),
                new YPositionInput(startingIndex++, defaultCapacity)
        ).collect(Collectors.toList());
    }
}
