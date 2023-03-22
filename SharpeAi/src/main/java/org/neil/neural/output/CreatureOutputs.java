package org.neil.neural.output;

import org.neil.neural.Output;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreatureOutputs implements OutputNodes{
    private int defaultCapacity =32;
    @Override
    public Collection<Output> outputs(int startingIndex) {
        return Stream.of( new LeftDirectionOutput(startingIndex++, defaultCapacity),
                new MoveOutput(startingIndex++, defaultCapacity),
                new RightDirectionOutput(startingIndex++, defaultCapacity)
        ).collect(Collectors.toList());
    }
}
