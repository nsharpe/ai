package org.neil.neural;

import java.util.Objects;
import java.util.Random;

public class Connection {
    private static Random random = new Random();
    private final Node source;
    private final Node destination;
    private final int bandwith;

    private final double multipler;
    private final ConnectionType connectionType;

    public Connection(Node source,
                      Node destination,
                      int bandwith,
                      double multipler,
                      ConnectionType connectionType) {
        this.source = Objects.requireNonNull(source);
        this.destination = Objects.requireNonNull(destination);
        this.bandwith = bandwith;
        this.connectionType = Objects.requireNonNull(connectionType);
        this.multipler = multipler;
    }

    public Node getSource() {
        return source;
    }

    public Node getDestination() {
        return destination;
    }

    public int getBandwith() {
        return bandwith;
    }

    public Connection copyNewMultipler(double newMultipler)
    {
        return new Connection(this.source, this.destination, this.bandwith, newMultipler, this.connectionType);
    }

    public Connection copyModifyBandWidth(int amount) {
        return new Connection(this.source, this.destination, bandwith + amount, multipler, connectionType);
    }

    public Connection copyNewBandWidth(int amount) {
        return new Connection(this.source, this.destination, bandwith + amount, multipler, connectionType);
    }

    public double getMultiplier() {
        return multipler;
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
                if (connectionType == ConnectionType.ADD) {
                    addLogic();
                } else {
                    subtractLogic();
                }
            }
        }
    }

    private void subtractLogic() {
        int toMove = bandwith < destination.getStored() ? bandwith : destination.getStored();
        toMove = toMove < source.getStored() ? toMove : source.getStored();

        if (toMove == 0) {
            return; //Destination is at capacity
        }

        source.addToStorage(-toMove);
        toMove *= multipler;
        toMove = toMove < destination.getStored() ? toMove : destination.getStored();
        destination.addToStorage(-toMove);
    }

    private void addLogic() {
        if (destination.getCapacity() == destination.getStored()) {
            return; //Destination is at capacity
        }

        int availableDesinationCapacity = destination.getCapacity() - destination.getStored();
        int maxToMove = bandwith < availableDesinationCapacity ? bandwith : availableDesinationCapacity;

        int toMove = maxToMove;
        if (source.getStored() <= maxToMove) {
            toMove = source.getStored();
        }

        source.addToStorage(-toMove);
        toMove *= multipler;
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
