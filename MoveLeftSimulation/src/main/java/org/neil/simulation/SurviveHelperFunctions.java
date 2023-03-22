package org.neil.simulation;

import org.neil.object.Creature;

public class SurviveHelperFunctions {

    public static Survive<Creature> leftMostSurvives() {
        return (simulation,creature)->
                xRangeSurvives(0, simulation.getCoordinateMap().xRange / 2).survives(simulation,creature);
    }

    public static Survive<Creature> xRangeSurvives(int xMin, int xMax) {
        return (simulation, creature) ->
                creature.getPosition().x >= xMin &&
                        creature.getPosition().x < xMax;

    }
}
