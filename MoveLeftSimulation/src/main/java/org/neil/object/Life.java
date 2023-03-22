package org.neil.object;

import org.neil.board.Coordinates;

public class Life {
    private volatile Coordinates position;
    private int energy;

    public Coordinates getPosition() {
        return position;
    }

    public void setPosition(Coordinates position) {
        this.position = position;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
