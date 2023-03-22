package org.neil.simulation;

import org.neil.neural.NetworkOwner;

@FunctionalInterface
public interface Survive<Sim extends SimulationEnvironment<?,E>, E extends NetworkOwner> {
    boolean survives(Sim simulation, E element);

    static Survive alwaysSurvive(){
        return (x,y) -> true;
    }

}
