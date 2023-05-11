package org.neil.neural;

import java.util.Objects;
public class Connection {
    private final Node source;
    private final Node destination;
    private final int bandwith;
    private final ConnectionType connectionType;

    public Connection(Node source,
                      Node destination,
                      int bandwith,
                      ConnectionType connectionType) {
        this.source = Objects.requireNonNull(source);
        this.destination = Objects.requireNonNull(destination);
        this.bandwith = bandwith;
        this.connectionType = Objects.requireNonNull(connectionType);
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

    public Connection copyModifyBandWidth(int amount) {
        return new Connection(this.source, this.destination, bandwith + amount, connectionType);
    }

    public Connection copyNewBandWidth(int amount) {
        return new Connection(this.source, this.destination, bandwith + amount, connectionType);
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

        toMove = -toMove;

        destination.addToStorage(toMove);
        source.addToStorage(toMove);
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

        destination.addToStorage(toMove);
        source.addToStorage(-toMove);
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public enum ConnectionType {
        ADD, SUBTRACT;

        public static ConnectionType random() {
            if (Math.random() > 0.5) {
                return ADD;
            }
            return SUBTRACT;
        }
    }

}
