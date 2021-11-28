package org.neil.neural;

public class Connection {
    private final Node source;
    private final Node destination;
    private final int bandwith;

    public Connection(Node source,
                      Node destination,
                      int bandwith) {
        this.source = source;
        this.destination = destination;
        this.bandwith = bandwith;
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
        return new Connection(this.source, this.destination, bandwith + amount);
    }

    public void activate() {
        if (source.getStored() <= 0) {
            return; // Nothing to move
        }
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
}
