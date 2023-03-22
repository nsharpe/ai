package org.neil.board;

import java.util.HashMap;
import java.util.Map;

public class Board <V>{
    public final int xRange;
    public final int yRange;

    private Map<Coordinates,V> boardState = new HashMap<>();

    public Board(int xRange, int yRange) {
        this.xRange = xRange;
        this.yRange = yRange;
    }

    public boolean isEmpty(Coordinates coordinates) {
        return !boardState.containsKey(coordinates);
    }

    public V get(Coordinates coordinates){
        return boardState.get(coordinates);
    }

    public boolean inBounds(Coordinates coordinates){
        return coordinates.x >= 0 && coordinates.x < xRange
                && coordinates.y >= 0 && coordinates.y < yRange;
    }

    public void place(Coordinates coordinates, V v){
        if(isEmpty(coordinates) && inBounds( coordinates)){
            boardState.put(coordinates,v);
        }
        else {
            throw new IllegalStateException("Could not move object");
        }
    }

    public void remove(Coordinates coordinates){
        boardState.remove(coordinates);
    }
}
