package org.neil.object;

import org.neil.map.Coordinates;

public class Life {
    private Coordinates position;
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
