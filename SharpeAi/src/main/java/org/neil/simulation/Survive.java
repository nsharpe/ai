package org.neil.simulation;

import org.neil.object.Creature;

@FunctionalInterface
public interface Survive<E> {
    boolean survives(Simulation<E> simulation, E element);

    static Survive alwaysSurvive(){
        return (x,y) -> true;
    }

}
