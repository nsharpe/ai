package org.neil.neural;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.neil.neural.output.OutputNode;
import org.neil.object.Creature;

public class OutputNodeTest {

    @Test
    public void testConsume(){
        OutputNode toTest = new OutputNodeNodeTester();

        toTest.addToStorage(101);
        Assertions.assertEquals(100, toTest.getStored());
        toTest.clearStorage();
        Assertions.assertEquals(0, toTest.getStored());
    }

    private static class OutputNodeNodeTester extends NodeDefault implements OutputNode<Creature> {
        public OutputNodeNodeTester() {
            super(1, 100);
        }

        @Override
        public void consumeOutput(Creature creature) {
            clearStorage();
        }

        public OutputNode copy(){
            return this;
        }
    }
}
