package org.neil.neural;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serial;

@JsonTypeName("nodeDivisor")
@JsonDeserialize(as=NodeDivisor.class)
public class NodeDivisor extends NodeDefault implements MutateableNode{

    @Serial
    private final static long serialVersionUID = 7173205259211958917L;

    public NodeDivisor(int id) {
        super(id);
    }

    public NodeDivisor(int id, int capacity, int activationLimit) {
        super(id, capacity, activationLimit);
    }

    @JsonCreator
    public NodeDivisor(@JsonProperty("@id") int id,
                       @JsonProperty("capacity")int capacity,
                       @JsonProperty("stored")int stored,
                       @JsonProperty("activateable") boolean activateable,
                       @JsonProperty("activationLimit") int activationLimit,
                       @JsonProperty("depreciate") int depreciate) {
        super(id, capacity, stored, activateable, activationLimit, depreciate);
    }

    public NodeDivisor(Node node) {
        super(node);
    }

    @Override
    public void addToStorage(int toAdd) {
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
}
