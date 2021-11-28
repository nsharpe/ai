package org.neil.neural.input;

import org.neil.map.Coordinates;
import org.neil.neural.Input;
import org.neil.neural.NodeDefault;
import org.neil.object.Direction;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectionViewInput extends NodeDefault implements Input {
    public DirectionViewInput(int capacity) {
        super(9, capacity);
    }

    @Override
    public void input(Inputs inputs) {
        clearStorage();

        Direction direction = inputs.direction;

        Set<Coordinates> currentLayer = Set.of(direction.position(inputs.coordinates));

        Set<Coordinates> nextLayer;

        for (int i = 0; i < 3; i++) {
            nextLayer = currentLayer.stream()
                    .flatMap(x -> Stream.of(direction.left().position(x),
                            direction.position(x),
                            direction.right().position(x)))
                    .collect(Collectors.toSet());
            int toAdd = availableCapacity() >> (i + 1);
            nextLayer.forEach(x->this.addToStorage(toAdd >= availableCapacity() ? 0 : toAdd));
            currentLayer = nextLayer;
        }

        if (inputs.coordinates.adjacent()
                .stream().anyMatch(x -> !inputs.coordinateMap.isEmpty(x))) {
            addToStorage(availableCapacity());
        }
    }

    @Override
    public Input copy() {
        return new DirectionViewInput(getCapacity());
    }
}
