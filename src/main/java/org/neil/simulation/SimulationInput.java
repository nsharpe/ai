package org.neil.simulation;

import org.neil.object.Creature;

import java.util.Comparator;

public class SimulationInput {
    public int x = 100;
    public int y = 100;

    public int maxNumberOfNodes = 10;
    public int maxNumberOfConnections = 100;
    public int runTime = 350;
    public int numberOfCreatures = 1400;
    public int numberOfRuns = 15000;
    public int maxNumberOfSurvivors = numberOfCreatures - (numberOfCreatures / 4);

    public double mutationRate = 0.05;

    public Comparator<Creature> survivorPriority = ReproductionPrioritization.euclidianCompare(x / 4, y / 2);

}
