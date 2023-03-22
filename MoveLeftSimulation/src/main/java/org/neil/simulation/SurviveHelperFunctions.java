package org.neil.simulation;

import org.neil.map.CoordinateMap;
import org.neil.object.Creature;

public class SurviveHelperFunctions {

    public static Survive<CoordinateMap,Creature> leftMostSurvives() {
        return (simulation,creature)->
                xRangeSurvives(0, simulation.xRange / 2).survives(simulation,creature);
    }

    public static Survive<CoordinateMap,Creature> xRangeSurvives(int xMin, int xMax) {
        return (simulation, creature) ->
                creature.getPosition().x >= xMin &&
                        creature.getPosition().x < xMax;

    }
}
