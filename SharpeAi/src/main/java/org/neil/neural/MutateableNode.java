package org.neil.neural;

import java.util.Random;

public interface MutateableNode extends Node{
    Random random = new Random();

    MutateableNode mutate(int capacityMin, int capacityMax, int activationMax);

    default int generateNewCapacity(int capacityMin, int capacityMax){
        return random.nextInt(capacityMax-capacityMin) + capacityMin;
    }

    default int generateNewActivation(int capacity, int activationMax){
        return random.nextInt(Math.min(capacity,activationMax));
    }
}
