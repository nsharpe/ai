package org.neil.neural;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;

public class DifferentialNode implements Node {

    private int previousValue = 0;
    private int stored;

    private final int id;
    private final int capacity;
    private final int activationThreshold;
    private final int depreciate;
    private final double mutationRate;

    public DifferentialNode(@JsonProperty("id")int id,
                            @JsonProperty("capacity")int capacity,
                            @JsonProperty("activationThreshold")int activationThreshold,
                            @JsonProperty("depreciate")int depreciate,
                            @JsonProperty("mutationRate") double mutationRate) {
        this.id = id;
        this.capacity = capacity;
        this.activationThreshold = activationThreshold;
        this.depreciate = depreciate;
        this.mutationRate = mutationRate;
    }

    @Override
    public void addToStorage(int toAdd) {
        long newStoredValue = stored + (previousValue - toAdd);
        previousValue = toAdd;
        if(newStoredValue > capacity) {
            newStoredValue = capacity;
        }
        if(newStoredValue < 0) {
            newStoredValue = 0;
        }
        stored = (int) newStoredValue;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getStored() {
        return stored;
    }

    @Override
    public void fillStorage() {
        stored = capacity;
    }

    @Override
    public Node copy() {
        return new DifferentialNode(id,
                capacity,
                activationThreshold,
                depreciate,
                mutationRate);
    }

    @Override
    public int getActivationLimit() {
        return activationThreshold;
    }

    @Override
    public void depreciate() {
        this.stored -= Math.min(this.stored,this.depreciate);
    }

    @Override
    public double mutationRate() {
        return mutationRate;
    }

    public static Mutator mutator(){
        return new Mutator();
    }

    public static class Mutator implements NodeMutator<DifferentialNode>{

        @Serial
        private static final long serialVersionUID = -8057973137161253840L;

        @Override
        public DifferentialNode generate(int id, int capacity) {
            return new DifferentialNode(id,capacity,capacity/2,Math.min(1,capacity/10),0);
        }
    }
}
