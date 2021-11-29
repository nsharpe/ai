package org.neil.neural;


import java.util.concurrent.atomic.AtomicInteger;

public class NodeDefault implements Node {
    private final int id;
    private final int capacity;
    private AtomicInteger stored = new AtomicInteger();

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

    public NodeDefault(Node node){
        this(node.getId(), node.getCapacity());
    }

    @Override
    public void addToStorage(int toAdd) {
        int stored = this.stored.addAndGet(toAdd);
        if (stored > capacity) {
            stored = capacity;
        }

        if (stored < 0) {
            throw new IllegalStateException("stored can't be less then zero");
        }
        if(stored > capacity){
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
        return stored.get();
    }

    @Override
    public void clearStorage() {
        stored.set(0);
    }

    @Override
    public void fillStorage() {
        stored.set(capacity);
    }

    @Override
    public Node copy() {
        return new NodeDefault(this);
    }
}
