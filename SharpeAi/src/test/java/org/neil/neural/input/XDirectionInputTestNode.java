package org.neil.neural.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.neil.object.Direction;

public class XDirectionInputTestNode {

    @Test
    public void testEastInput(){
        XDirectionInputNode xDirectionInput = new XDirectionInputNode(1,128);

        xDirectionInput.input(input(Direction.NEUTRAL_EAST));
        Assertions.assertEquals(128,xDirectionInput.getStored());
    }

    @Test
    public void testNeutralInput(){
        XDirectionInputNode xDirectionInput = new XDirectionInputNode(1,128);

        xDirectionInput.input(input(Direction.NORTH_NEUTRAL));
        Assertions.assertEquals(64,xDirectionInput.getStored());
    }

    @Test
    public void testWestInput(){
        XDirectionInputNode xDirectionInput = new XDirectionInputNode(1,128);

        xDirectionInput.input(input(Direction.NORTH_WEST));
        Assertions.assertEquals(0,xDirectionInput.getStored());
    }

    private Inputs input(Direction direction) {
        Inputs inputs = new Inputs();
        inputs.direction = direction;
        return inputs;
    }
}
