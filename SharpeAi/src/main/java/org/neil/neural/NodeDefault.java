package org.neil.neural;


import java.util.Objects;

public class NodeDefault implements Node {
    private final int id;
    private final int capacity;
    private volatile int stored = 0;
    private volatile boolean activateable;

    private final int activationLimit;

    private final int depreciate;

    public NodeDefault(int id) {
        this(id, Integer.MAX_VALUE, Integer.MAX_VALUE / 2);
    }

    public NodeDefault(int id, int capacity, int activationLimit) {
        if (id <= 0) {
            throw new IllegalStateException("id must be positive");
        }
        if (capacity < 0) {
            throw new IllegalStateException("capcity must be positive");
        }
        this.id = id;
        this.capacity = capacity;
        this.activationLimit = activationLimit;
        this .depreciate = activationLimit / 5;
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
