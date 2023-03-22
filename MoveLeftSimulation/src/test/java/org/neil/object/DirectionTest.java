package org.neil.object;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DirectionTest {

    @Test
    public void testLeft(){
        Assertions.assertEquals(Direction.NEUTRAL_WEST,Direction.NORTH_WEST.left());
    }

    @Test
    public void testRight(){
        Assertions.assertEquals(Direction.NORTH_WEST,Direction.NEUTRAL_WEST.right());
    }

    @Test
    public void testNoErrorsThrown(){
        for(Direction direction: Direction.values()){
            direction.left();
            direction.right();
        }
    }
}
