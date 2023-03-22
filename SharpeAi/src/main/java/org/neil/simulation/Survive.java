package org.neil.simulation;

import org.neil.object.Creature;

@FunctionalInterface
public interface Survive {
    boolean survives(Simulation simulation, Creature creature);

    default Survive and(Survive survives) {
        return (x, y) -> this.survives(x, y) && survives.survives(x, y);
    }

    static Survive alwaysSurvive(){
        return (x,y) -> true;
    }

    static Survive leftMostSurvives() {
        return (simulation,creature)->
                xRangeSurvives(0, simulation.getCoordinateMap().xRange / 2).survives(simulation,creature);
    }

    static Survive xRangeSurvives(int xMin, int xMax) {
        return (simulation, creature) ->
                creature.getPosition().x >= xMin &&
                creature.getPosition().x < xMax;

    }
}
