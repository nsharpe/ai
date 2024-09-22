package org.neil.neural;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import org.neil.neural.serializer.NodeDeserializer;

import java.util.Objects;

@JsonTypeName("default")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(alphabetic = true)
@JsonDeserialize(as= NodeDefault.class)
@JsonIdentityInfo( generator= ObjectIdGenerators.PropertyGenerator.class,scope = Node.class)
public class NodeDefault implements Node {

    private final int id;
    @JsonProperty("capacity")
    private final int capacity;
    @JsonProperty("stored")
    private volatile int stored = 0;
    @JsonProperty("activateable")
    private volatile boolean activateable;

    @JsonProperty("activationLimit")
    private final int activationLimit;

    @JsonProperty("depreciate")
    private final int depreciate;

    public NodeDefault(int id) {
        this(id, Integer.MAX_VALUE, Integer.MAX_VALUE / 2);
    }

    public NodeDefault(int id,
                       int capacity,
                       int activationLimit) {
        this(id,
                capacity,
                0,
                false,
                activationLimit,
                activationLimit/5);
    }

    @JsonCreator()
    public NodeDefault(@JsonProperty("@id") int id,
                       @JsonProperty("capacity")int capacity,
                       @JsonProperty("stored")int stored,
                       @JsonProperty("activateable") boolean activateable,
                       @JsonProperty("activationLimit") int activationLimit,
                       @JsonProperty("depreciate") int depreciate) {
        if (id <= 0) {
            throw new IllegalStateException("id must be positive");
        }
        if (capacity < 0) {
            throw new IllegalStateException("capcity must be positive");
        }
        this.id = id;
        this.capacity = capacity;
        this.stored = stored;
        this.activateable = activateable;
        this.activationLimit = activationLimit;
        this.depreciate = depreciate;
    }

    public NodeDefault(Node node) {
        this(node.getId(), node.getCapacity(), node.getActivationLimit());
    }

    @Override
    public void addToStorage(int toAdd) {
        stored += toAdd;
        if (stored > capacity) {
            stored = capacity;
        }

        if (stored > capacity / 2) {
            activateable = true;
        }
        if (stored == 0) {
            activateable = false;
        }

        if (stored < 0) {
            throw new IllegalStateException("stored can't be less then zero");
        }
        if (stored > capacity) {
            throw new IllegalStateException("stored can't be greater then capacity");
        }
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
    public void clearStorage() {
        stored = 0;
    }
    public int getActivationLimit() {
        return activationLimit;
    }

    @Override
    public void fillStorage() {
        stored = capacity;
    }

    public boolean isActivateable() {
        return activateable;
    }

    @Override
    public void depreciate() {
        this.stored -= Math.min(this.stored,this.depreciate);
    }

    @Override
    public Node copy() {
        return new NodeDefault(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeDefault that = (NodeDefault) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
