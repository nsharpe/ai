package org.neil.neural;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serial;

@JsonTypeName("nodeDivisor")
@JsonDeserialize(as=NodeDivisor.class)
public class NodeDivisor extends AbstractNode implements MutateableNode{

    @Serial
    private final static long serialVersionUID = 7173205259211958917L;

    public NodeDivisor(int id) {
        super(id,0);
    }

    public NodeDivisor(int id, int capacity, int activationLimit) {
        super(id, capacity, activationLimit,0);
    }

    @JsonCreator
    public NodeDivisor(@JsonProperty("@id") int id,
                       @JsonProperty("capacity")int capacity,
                       @JsonProperty("stored")int stored,
                       @JsonProperty("activateable") boolean activateable,
                       @JsonProperty("activationLimit") int activationLimit,
                       @JsonProperty("depreciate") int depreciate,
                       @JsonProperty("mutationRate") double mutationRate) {
        super(id, capacity, stored, activateable, activationLimit, depreciate,mutationRate);
    }

    public NodeDivisor(Node node) {
        super(node);
    }

    @Override
    public synchronized void addToStorage(int toAdd) {
        if(toAdd > 0) {
            toAdd = toAdd >> 1;
        }
        if(toAdd > getCapacity() - getStored()){
            toAdd = getCapacity() - getStored();
        }
        super.addToStorage(toAdd);
    }

    @Override
    public MutateableNode mutate(int capacityMin, int capacityMax, int activationMax) {
        int capacity = generateNewCapacity(capacityMin,capacityMax);
        return new NodeDivisor(this.getId(),capacity,generateNewActivation(capacity,activationMax));
    }

    @Override
    public Node copy() {
        return new NodeDivisor(this);
    }

    public static Mutator mutator(){
        return new Mutator();
    }

    public static class Mutator implements NodeMutator<NodeDivisor>{

        @Serial
        private static final long serialVersionUID = -8057973137161253840L;

        @Override
        public NodeDivisor generate(int id, int capacity) {
            return new NodeDivisor(id,capacity,capacity/2);
        }
    }
}
