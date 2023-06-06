package org.neil.simulation;

import org.neil.neural.input.InputNodeGenerator;
import org.neil.neural.output.OutputNodeGenerator;

import java.util.Comparator;
import java.util.function.Function;

public class SimulationInput<I,O> {
    public int x = 200;
    public int y = 200;

    public int runTime = 300;
    public int numberOfRuns = 15000;

    public Comparator<O> survivorPriority;
    public Survive surviveLogic;

    public Function<Simulation,Integer> numberOfElements = x -> 1500;
    public Function<Simulation,Integer> numberOfSurvivors = x -> numberOfElements.apply(x);

    public InputNodeGenerator<I> inputNodeGenerator;
    public OutputNodeGenerator<O> outputNodeGenerator;

    public MutationStrategy mutationStrategy = MutationStrategy.FIRST_CHILD_NO_MUTATIONS;


}
