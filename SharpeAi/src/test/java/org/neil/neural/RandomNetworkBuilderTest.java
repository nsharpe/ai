package org.neil.neural;

import org.junit.jupiter.api.Test;
import org.neil.simulation.MutationStrategy;
import org.neil.simulation.SimulationInput;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class RandomNetworkBuilderTest {

    @Test
    public void testCopyDoesNotUseSourceInputNodes() {
        SimulationInput simulationInput = new SimulationInput();

        simulationInput.inputNodeGenerator = x -> Arrays.asList();
        simulationInput.outputNodeGenerator = x -> Arrays.asList();

        RandomNetworkBuilder builder = new RandomNetworkBuilder(simulationInput);
        builder.maxNodes(201)
                .minNodes(200);

        Network source = builder.build();
        Network copy = builder.copyWithChanceToMutate(source);

        for (Node node : source.getInputs()) {
            assertFalse(copy.getInputs().contains(node));
        }
    }

    @Test
    public void testCopyDoesNotUseSourceIntermediateNodes() {
        SimulationInput simulationInput = new SimulationInput();

        simulationInput.inputNodeGenerator = x -> Arrays.asList();
        simulationInput.outputNodeGenerator = x -> Arrays.asList();
        simulationInput.mutationStrategy = MutationStrategy.NONE;

        RandomNetworkBuilder builder = new RandomNetworkBuilder(simulationInput);
        builder.maxNodes(201)
                .minNodes(200);
        Network source = builder.build();
        Network copy = builder.copyWithChanceToMutate(source);

        for(int i = 0; i < source.getIntermediateNodes().size(); i++){
            Node sourceNode = source.getIntermediateNodes().get(i);
            Node copyNode = copy.getIntermediateNodes().get(i);
            assertEquals(sourceNode, copyNode);
            assertFalse(sourceNode == copyNode);
        }
    }

    @Test
    public void testCopyDoesNotUseSourceOutputNodes() {
        SimulationInput simulationInput = new SimulationInput();

        simulationInput.inputNodeGenerator = x -> Arrays.asList();
        simulationInput.outputNodeGenerator = x -> Arrays.asList();

        RandomNetworkBuilder builder = new RandomNetworkBuilder(simulationInput);

        builder.maxNodes(201)
                .minNodes(200);

        Network source = builder.build();
        Network copy = builder.copyWithChanceToMutate(source);

        for (Node node : source.getOutputs()) {
            assertFalse(copy.getOutputs().contains(node));
        }

    }
}
