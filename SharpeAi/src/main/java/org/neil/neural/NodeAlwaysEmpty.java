package org.neil.neural;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeName("alwaysEmpty")
@JsonDeserialize(as= NodeAlwaysEmpty.class)
public class NodeAlwaysEmpty extends NodeDefault{

    public NodeAlwaysEmpty(int id) {
        super(id);
    }

    public NodeAlwaysEmpty(int id, int capacity) {
        super(id, capacity, capacity / 2);
    }

    public NodeAlwaysEmpty(Node node) {
        super(node);
    }

    @JsonCreator
    public NodeAlwaysEmpty(@JsonProperty("@id") int id,
                           @JsonProperty("capacity")int capacity,
                           @JsonProperty("stored")int stored,
                           @JsonProperty("activateable") boolean activateable,
                           @JsonProperty("activationLimit") int activationLimit,
                           @JsonProperty("depreciate") int depreciate) {
        super(id, capacity, stored, activateable, activationLimit, depreciate);
    }

    @Override
    public void addToStorage(int toAdd) {
        // noop
    }

    @Override
    public int availableCapacity() {
        return getCapacity();
    }

    @Override
    public Node copy() {
        return new NodeAlwaysEmpty(this);
    }
}
