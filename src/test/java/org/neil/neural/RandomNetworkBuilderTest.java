package org.neil.neural;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class RandomNetworkBuilderTest {

    @Test
    public void testCopyDoesNotUseSourceInputNodes(){
        RandomNetworkBuilder builder = new RandomNetworkBuilder();
        builder.maxNodes(200);
        builder.minNodes(200);

        Network source = builder.build();
        Network copy =  builder.copyWithChanceToMutate(source);

        for(Node node: source.getInputs()){
            assertFalse(copy.getInputs().contains(node));
        }
    }

    @Test
    public void testCopyDoesNotUseSourceIntermediateNodes(){
        RandomNetworkBuilder builder = new RandomNetworkBuilder();
        builder.maxNodes(200);
        builder.minNodes(200);

        Network source = builder.build();
        Network copy =  builder.copyWithChanceToMutate(source);

        for(Node node: source.getIntermediateNodes()){
            assertFalse(copy.getIntermediateNodes().contains(node));
        }
    }

    @Test
    public void testCopyDoesNotUseSourceOutputNodes(){
        RandomNetworkBuilder builder = new RandomNetworkBuilder();
        builder.maxNodes(200);
        builder.minNodes(200);

        Network source = builder.build();
        Network copy =  builder.copyWithChanceToMutate(source);

        for(Node node: source.getOutputs()){
            assertFalse(copy.getOutputs().contains(node));
        }

    }
}
