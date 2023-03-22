package org.neil.neural.input;

import org.neil.map.Coordinates;
import org.neil.neural.NodeDefault;
import org.neil.object.Direction;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectionViewInputNode extends NodeDefault implements InputNode {
    public DirectionViewInputNode(int id, int capacity) {
        super(id, capacity);
    }

    @Override
    public void input(Inputs inputs) {
        clearStorage();

        Direction direction = inputs.direction;

        Set<Coordinates> currentLayer = Set.of(direction.position(inputs.coordinates));

        Set<Coordinates> nextLayer;

        for (int i = 0; i < 1; i++) {
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
    public InputNode copy() {
        return new DirectionViewInputNode(getId(), getCapacity());
    }
}
