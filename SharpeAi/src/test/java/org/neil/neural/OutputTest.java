package org.neil.neural;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.neil.object.Creature;

public class OutputTest {

    @Test
    public void testConsume(){
        Output toTest = new OutputNodeTester();

        toTest.addToStorage(101);
        Assertions.assertEquals(100, toTest.getStored());
        toTest.clearStorage();
        Assertions.assertEquals(0, toTest.getStored());
    }

    private static class OutputNodeTester extends NodeDefault implements Output{
        public OutputNodeTester() {
            super(1, 100);
        }

        @Override
        public void consumeOutput(Creature creature) {
            clearStorage();
        }

        public Output copy(){
            return this;
        }
    }
}
