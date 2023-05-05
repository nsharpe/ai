package org.neil.neural;


public class NodeDefault implements Node {
    private final int id;
    private final int capacity;
    private volatile int stored = 0;
    private volatile boolean activateable;

    public NodeDefault(int id) {
        this(id, Integer.MAX_VALUE);
    }

    public NodeDefault(int id, int capacity) {
        if (id <= 0) {
            throw new IllegalStateException("id must be positive");
        }
        if (capacity < 0) {
            throw new IllegalStateException("capcity must be positive");
        }
        this.id = id;
        this.capacity = capacity;
    }

    public NodeDefault(Node node) {
        this(node.getId(), node.getCapacity());
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

    @Override
    public void fillStorage() {
        stored = capacity;
    }

    public boolean isActivateable() {
        return activateable;
    }

    @Override
    public Node copy() {
        return new NodeDefault(this);
    }
}
