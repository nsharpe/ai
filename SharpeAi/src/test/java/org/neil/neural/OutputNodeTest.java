package org.neil.neural;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.neil.neural.output.OutputNode;

public class OutputNodeTest {

    @Test
    public void testConsume(){
        OutputNode toTest = new OutputNodeNodeTester();

        toTest.addToStorage(101);
        Assertions.assertEquals(100, toTest.getStored());
        toTest.clearStorage();
        Assertions.assertEquals(0, toTest.getStored());
    }

    private static class OutputNodeNodeTester extends AbstractNode implements OutputNode<Integer> {
        public OutputNodeNodeTester() {
            super(1, 100,1);
        }

        @Override
        public void consumeOutput(Integer creature) {
            clearStorage();
        }

        public OutputNode copy(){
            return this;
        }
    }
}
