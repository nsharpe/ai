package org.neil.neural.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.neil.object.Direction;

public class YDirectionInputTestNode {

    @Test
    public void testNorthInput() {
        YDirectionInputNode yDirectionInput = new YDirectionInputNode(1,128);

        yDirectionInput.input(input(Direction.NORTH_WEST));
        Assertions.assertEquals(128, yDirectionInput.getStored());
    }

    @Test
    public void testNeutralInput() {
        YDirectionInputNode yDirectionInput = new YDirectionInputNode(1,128);

        yDirectionInput.input(input(Direction.NEUTRAL_EAST));
        Assertions.assertEquals(64, yDirectionInput.getStored());
    }

    @Test
    public void testSouthInput() {
        YDirectionInputNode yDirectionInput = new YDirectionInputNode(1,128);

        yDirectionInput.input(input(Direction.SOUTH_EAST));
        Assertions.assertEquals(0, yDirectionInput.getStored());
    }

    private Inputs input(Direction direction) {
        Inputs inputs = new Inputs();
        inputs.direction = direction;
        return inputs;
    }
}
