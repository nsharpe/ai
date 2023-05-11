package org.neil.object;

import org.neil.board.Coordinates;

public class Life {
    private volatile Coordinates position;
    private int energy;
    private Coordinates startPosition;

    public Coordinates getPosition() {
        return position;
    }

    public Coordinates getStartPosition() {
        return startPosition;
    }

    public void setPosition(Coordinates position) {
        if(startPosition == null){
            this.startPosition = position;
        }
        this.position = position;
    }

    public boolean hasMoved(){
        return !this.position.equals(startPosition);
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
