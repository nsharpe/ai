package org.neil.neural;

import org.junit.jupiter.api.Test;
import org.neil.simulation.SimulationInput;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class RandomNetworkBuilderTest {

    @Test
    public void testCopyDoesNotUseSourceInputNodes() {
        SimulationInput simulationInput = new SimulationInput();

        RandomNetworkBuilder builder = new RandomNetworkBuilder(simulationInput);
        builder.minNodes(200);

        Network source = builder.build();
        Network copy = builder.copyWithChanceToMutate(source);

        for (Node node : source.getInputs()) {
            assertFalse(copy.getInputs().contains(node));
        }
    }

    @Test
    public void testCopyDoesNotUseSourceIntermediateNodes() {
        SimulationInput simulationInput = new SimulationInput();

        RandomNetworkBuilder builder = new RandomNetworkBuilder(simulationInput);
        builder.minNodes(200);

        Network source = builder.build();
        Network copy = builder.copyWithChanceToMutate(source);

        for (Node node : source.getIntermediateNodes()) {
            assertFalse(copy.getIntermediateNodes().contains(node));
        }
    }

    @Test
    public void testCopyDoesNotUseSourceOutputNodes() {
        SimulationInput simulationInput = new SimulationInput();

        RandomNetworkBuilder builder = new RandomNetworkBuilder(simulationInput);
        builder.minNodes(200);

        Network source = builder.build();
        Network copy = builder.copyWithChanceToMutate(source);

        for (Node node : source.getOutputs()) {
            assertFalse(copy.getOutputs().contains(node));
        }

    }
}
