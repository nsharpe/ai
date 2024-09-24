package org.neil.neural;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Random;


public class Connection implements Serializable {

    @Serial
    private final static long serialVersionUID = -2250361909761596387L;

    private static Random random = new Random();

    private final Node source;

    private final Node destination;

    @JsonProperty
    private final int bandwidth;
    @JsonProperty
    private final double multiplier;
    @JsonProperty
    private final ConnectionType connectionType;

    @JsonCreator
    public Connection(@JsonProperty("source") Node source,
                      @JsonProperty("destination") Node destination,
                      @JsonProperty("bandwidth") int bandwidth,
                      @JsonProperty("multiplier") double multiplier,
                      @JsonProperty("connectionType") ConnectionType connectionType) {
        this.source = Objects.requireNonNull(source);
        this.destination = Objects.requireNonNull(destination);
        this.bandwidth = bandwidth;
        this.connectionType = Objects.requireNonNull(connectionType);
        this.multiplier = multiplier;
    }

    @JsonIgnore
    public Node getSource() {
        return source;
    }

    @JsonProperty("source")
    public int getSourceId(){
        return source.getId();
    }

    @JsonIgnore
    public Node getDestination() {
        return destination;
    }

    @JsonProperty("destination")
    public int getDestinationId(){
        return destination.getId();
    }


    public int getBandwidth() {
        return bandwidth;
    }

    public Connection copyNewMultiplier(double newMultipler)
    {
        return new Connection(this.source, this.destination, this.bandwidth, newMultipler, this.connectionType);
    }

    public Connection copyModifyBandWidth(int amount) {
        return new Connection(this.source, this.destination, bandwidth + amount, multiplier, connectionType);
    }

    public Connection copyNewBandWidth(int amount) {
        return new Connection(this.source, this.destination, bandwidth + amount, multiplier, connectionType);
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void activate() {
        if (source == destination) {
            return; // Nothing to move
        }

        Node lock1 = source.getId() < destination.getId() ? source : destination;
        Node lock2 = source == lock1 ? destination : source;

        if (!source.isActivateable() || source.getStored() == 0) {
            return;
        }

        synchronized (lock1) {
            synchronized (lock2) {
                if (!source.isActivateable() || source.getStored() == 0) {
                    return;
                }
                switch (connectionType){
                    case ADD -> addLogic();
                    case SUBTRACT -> subtractLogic();
                    default -> throw new IllegalStateException("Not supported");
                }
            }
        }
    }

    private void subtractLogic() {
        int toMove = bandwidth < destination.getStored() ? bandwidth : destination.getStored();
        toMove = toMove < source.getStored() ? toMove : source.getStored();

        if (toMove == 0) {
            return; //Destination is at capacity
        }

        source.addToStorage(-toMove);
        toMove *= multiplier;
        toMove = toMove < destination.getStored() ? toMove : destination.getStored();
        destination.addToStorage(-toMove);
    }

    private void addLogic() {
        if (destination.getCapacity() == destination.getStored()) {
            return; //Destination is at capacity
        }

        int availableDesinationCapacity = destination.getCapacity() - destination.getStored();
        int maxToMove = bandwidth < availableDesinationCapacity ? bandwidth : availableDesinationCapacity;

        int toMove = maxToMove;
        if (source.getStored() <= maxToMove) {
            toMove = source.getStored();
        }

        source.addToStorage(-toMove);
        toMove *= multiplier;
        destination.addToStorage(toMove);
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public enum ConnectionType {
        ADD, SUBTRACT;

        public static ConnectionType random() {
            if (random.nextBoolean()) {
                return ADD;
            }
            return SUBTRACT;
        }
    }

}
