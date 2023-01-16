package org.neil.simulation;

import org.neil.object.Creature;

import java.util.Comparator;

public class SimulationInput {
    public int x = 100;
    public int y = 100;

    public int runTime = 250;
    public int numberOfCreatures = 500;
    public int numberOfRuns = 15000;
    public int maxNumberOfSurvivors = 25;

    public Comparator<Creature> survivorPriority = Comparator.comparing(x->x.getPosition().x);
}
