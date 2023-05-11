package org.neil.neural;

public interface Node {
    void addToStorage(int toAdd);

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

    boolean isActivateable();

    int getActivationLimit();

    void depreciate();
}
