package org.neil.simulation;

import org.neil.neural.input.InputNodeGenerator;
import org.neil.neural.output.OutputNodeGenerator;

import java.util.Comparator;
import java.util.function.Function;

public class SimulationInput<I,O> {
    public int x = 200;
    public int y = 200;

    public int maxNumberOfNodes = 90;
    public int maxNumberOfConnections = 1200;
    public int runTime = 300;
    public int numberOfRuns = 15000;

    public double mutationRate = 0.3;

    public Comparator<O> survivorPriority;
    public Survive surviveLogic;

    public Function<Simulation,Integer> numberOfElements = x -> 1500;
    // every 100 generations ensure that some elements die
//    public Function<Simulation,Integer> numberOfSurvivors = x -> x.getRunsCompleted() % 100 == 0?
//            numberOfElements.apply(x) >> 1 :  numberOfElements.apply(x);
    public Function<Simulation,Integer> numberOfSurvivors = x -> numberOfElements.apply(x);

    public InputNodeGenerator<I> inputNodeGenerator;
    public OutputNodeGenerator<O> outputNodeGenerator;

//    public Function<Simulation,Integer> numberOfCreatures = x -> x.getRunsCompleted() % 1000 > 900 ? 400 : 1400;
//    public Function<Simulation,Integer> numberOfSurvivors
//            = x -> x.getRunsCompleted() < 900 ? 1000 - x.getRunsCompleted() / 2 :  500;

//    public Function<Simulation,Integer> numberOfSurvivors
//            = x -> Math.max( numberOfCreatures - 100 - (int)( Math.log(x.getRunsCompleted()) * 100), numberOfCreatures / 10);

//    public Comparator<Creature> survivorPriority = ReproductionPrioritization.euclidianCompare(x / 4, y / 2);
//    public Survive surviveLogic = Survive.alwaysSurvive();

}
