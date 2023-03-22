package org.neil.simulation;

import org.neil.map.CoordinateMap;
import org.neil.object.Creature;

public class SurviveHelperFunctions {

    public static Survive<Creature> leftMostSurvives(CoordinateMap coordinateMap) {
        return (simulation,creature)->
                xRangeSurvives(0, coordinateMap.xRange / 2).survives(simulation,creature);
    }

    public static Survive<Creature> xRangeSurvives(int xMin, int xMax) {
        return (simulation, creature) ->
                creature.getPosition().x >= xMin &&
                        creature.getPosition().x < xMax;

    }
}
