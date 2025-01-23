package org.neil.neural;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@JsonTypeName("nodeMax")
@JsonDeserialize(as= NodeMax.class)
public class NodeMax extends AbstractNode implements MutateableNode{

    @Serial
    private static final long serialVersionUID = 820838137585241510L;

    @JsonProperty
    private int numberOfInputsKept = 5;
    @JsonProperty
    private List<Integer> lastInputs = new ArrayList<>();

    @JsonProperty
    private volatile Integer currentMax = 0;

    public NodeMax(int id, double mutationRate) {
        super(id,mutationRate);
    }

    public NodeMax(int id, int capacity, int activation, double mutationRate) {
        super(id, capacity, activation,mutationRate);
    }

    @JsonCreator
    public NodeMax(@JsonProperty("@id") int id,
                   @JsonProperty("capacity")int capacity,
                   @JsonProperty("stored")int stored,
                   @JsonProperty("activateable") boolean activateable,
                   @JsonProperty("activationLimit") int activationLimit,
                   @JsonProperty("depreciate") int depreciate,
                   @JsonProperty("numberOfInputsKept") int numberOfInputsKept,
                   @JsonProperty("lastInputs") List<Integer> lastInputs,
                   @JsonProperty("currentMax") Integer currentMax,
                   @JsonProperty("mutationRate") double mutationRate) {
        super(id, capacity, stored, activateable, activationLimit, depreciate, mutationRate);
        this.numberOfInputsKept = numberOfInputsKept;
        this.lastInputs = lastInputs;
        this.currentMax = currentMax;
    }

    public NodeMax(Node node) {
        super(node);
    }

    @Override
    public synchronized void addToStorage(int toAdd) {
        if(toAdd > getCapacity()){
            toAdd = getCapacity();
        }
        if(lastInputs.size() > numberOfInputsKept){
            lastInputs.remove(0);
        }
        lastInputs.add(toAdd);

        setCurrentMax();
    }

    private void setCurrentMax(){
        currentMax = lastInputs.stream()
                .mapToInt(x->x)
                .filter( x -> x > 0)
                .max()
                .orElse(0);
    }

    @Override
    public boolean isActivateable() {
        return currentMax > getActivationLimit();
    }

    @Override
    public int getStored() {
        return currentMax;
    }

    @Override
    public Node copy() {
        return new NodeMax(this);
    }

    @Override
    public MutateableNode mutate(int capacityMin, int capacityMax, int activationMax) {
        int capacity = generateNewCapacity(capacityMin,capacityMax);
        return new NodeMax(this.getId(),capacity,generateNewActivation(capacity,activationMax),0.0);
    }


    public static Mutator mutator(){
        return new Mutator();
    }

    public static class Mutator implements NodeMutator<NodeMax>{

        @Serial
        private static final long serialVersionUID = 5266982723203463212L;

        @Override
        public NodeMax generate(int id, int capacity) {
            return new NodeMax(id,capacity,capacity/2,0);
        }
    }
}
