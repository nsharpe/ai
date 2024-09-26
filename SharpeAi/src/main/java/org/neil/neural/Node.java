package org.neil.neural;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.neil.neural.serializer.NodeDeserializer;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NodeAlwaysEmpty.class, name = "alwaysEmpty"),
        @JsonSubTypes.Type(value = NodeAlwaysFull.class, name = "alwaysFull"),
        @JsonSubTypes.Type(value = AbstractNode.class, name = "default"),
        @JsonSubTypes.Type(value = NodeDivisor.class,name = "nodeDivisor"),
        @JsonSubTypes.Type(value = NodeMultiplier.class,name="nodeMultiplier"),
        @JsonSubTypes.Type(value = NodeMax.class, name="nodeMax"),
}
)
@JsonDeserialize(using = NodeDeserializer.class)
@JsonIdentityInfo( generator= ObjectIdGenerators.PropertyGenerator.class,scope = Node.class)
public interface Node extends Serializable {
    void addToStorage(int toAdd);

    @JsonProperty("@id")
    int getId();

    int getCapacity();

    int getStored();

    default int availableCapacity(){
        return getCapacity() - getStored();
    }

    default void clearStorage(){
        addToStorage(-getStored());
    }

    void fillStorage();

    Node copy();

    default boolean isActivateable(){
        return getActivationLimit() < getStored();
    }

    int getActivationLimit();

    void depreciate();
}
