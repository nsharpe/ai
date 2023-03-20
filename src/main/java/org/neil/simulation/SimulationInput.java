package org.neil.simulation;

import org.neil.object.Creature;

import java.util.Comparator;
import java.util.function.Function;

public class SimulationInput {
    public int x = 150;
    public int y = 150;

    public int maxNumberOfNodes = 30;
    public int maxNumberOfConnections = 100;
    public int runTime = 350;
    public int numberOfRuns = 15000;

    public double mutationRate = 0.015;

    public Comparator<Creature> survivorPriority = ReproductionPrioritization.xCompare();
    public Survive surviveLogic = Survive.leftMostSurvives();

    public Function<Simulation,Integer> numberOfCreatures = x -> 2500;
    public Function<Simulation,Integer> numberOfSurvivors = x -> 1500;

//    public Function<Simulation,Integer> numberOfCreatures = x -> x.getRunsCompleted() % 1000 > 900 ? 400 : 1400;
//    public Function<Simulation,Integer> numberOfSurvivors
//            = x -> x.getRunsCompleted() < 900 ? 1000 - x.getRunsCompleted() / 2 :  500;

//    public Function<Simulation,Integer> numberOfSurvivors
//            = x -> Math.max( numberOfCreatures - 100 - (int)( Math.log(x.getRunsCompleted()) * 100), numberOfCreatures / 10);

//    public Comparator<Creature> survivorPriority = ReproductionPrioritization.euclidianCompare(x / 4, y / 2);
//    public Survive surviveLogic = Survive.alwaysSurvive();

}
