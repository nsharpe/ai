package org.neil.neural.output;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreatureOutputs implements OutputNodeGenerator {
    private int defaultCapacity =32;
    @Override
    public Collection<OutputNode> outputs(int startingIndex) {
        return Stream.of( new LeftDirectionOutputNode(startingIndex++, defaultCapacity),
                new MoveOutputNode(startingIndex++, defaultCapacity),
                new RightDirectionOutputNode(startingIndex++, defaultCapacity)
        ).collect(Collectors.toList());
    }
}
