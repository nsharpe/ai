package org.neil.simulation;

import org.neil.neural.Network;
import org.neil.neural.NetworkOwner;

import java.util.Collection;

public interface SimulationEnvironment <K,V extends NetworkOwner>{

    Collection<V> getValues();

    default void preStepAction(){
        //noop
    }

    default void postStepAction(){
        //noop
    }

    void addElement(Network neuralNetwork);

    public void generationEnds();
}
