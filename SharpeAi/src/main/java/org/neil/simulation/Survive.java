package org.neil.simulation;

import org.neil.neural.NetworkOwner;

@FunctionalInterface
public interface Survive<E extends NetworkOwner> {
    boolean survives(Simulation<?,E> simulation, E element);

    static Survive alwaysSurvive(){
        return (x,y) -> true;
    }

}
