package org.neil.neural;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serial;

@JsonTypeName("nodeMultiplier")
@JsonDeserialize(as=NodeMultiplier.class)
public class NodeMultiplier extends AbstractNode implements MutateableNode{

    @Serial
    private final static long serialVersionUID = 1395182505655397178L;

    public NodeMultiplier(int id,double mutationRate) {
        super(id,mutationRate);
    }

    public NodeMultiplier(int id, int capacity, int activation,double mutationRate) {
        super(id, capacity, activation,mutationRate);
    }

    public NodeMultiplier(@JsonProperty("@id") int id,
                          @JsonProperty("capacity")int capacity,
                          @JsonProperty("stored")int stored,
                          @JsonProperty("activateable") boolean activateable,
                          @JsonProperty("activationLimit") int activationLimit,
                          @JsonProperty("depreciate") int depreciate,
                          @JsonProperty("mutationRate") double mutationRate) {
        super(id, capacity, stored, activateable, activationLimit, depreciate,mutationRate);
    }

    public NodeMultiplier(Node node) {
        super(node);
    }

    @Override
    public synchronized void addToStorage(int toAdd) {
        if(toAdd > 0) {
            toAdd = toAdd << 1;
        }
        if(toAdd > getCapacity() - getStored()){
            toAdd = getCapacity() - getStored();
        }
        super.addToStorage(toAdd);
    }

    @Override
    public Node copy() {
        return new NodeMultiplier(this);
    }

    @Override
    public MutateableNode mutate(int capacityMin, int capacityMax, int activationMax) {
        int capacity = generateNewCapacity(capacityMin,capacityMax);
        return new NodeMultiplier(this.getId(),capacity,generateNewActivation(capacity,activationMax),0);
    }


    public static Mutator mutator(){
        return new Mutator();
    }

    public static class Mutator implements NodeMutator<NodeMultiplier>{

        @Serial
        private static final long serialVersionUID = 9059213204079128560L;

        @Override
        public NodeMultiplier generate(int id, int capacity) {
            return new NodeMultiplier(id,capacity,capacity/2,0);
        }
    }
}
