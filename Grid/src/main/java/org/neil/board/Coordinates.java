package org.neil.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Coordinates {
    public final int x;
    public final int y;
    private int _hash;
    private volatile Set<Coordinates> _adjacent;

    private static Map<Integer, Map<Integer, Coordinates>> cache = Collections.synchronizedMap(new HashMap<>());

    private Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
        this._hash = Objects.hash(x, y);
    }

    public static Coordinates of(final int x, final int y) {
        return cache.computeIfAbsent(x, (key) -> Collections.synchronizedMap(new HashMap<>()))
                .computeIfAbsent(y, key -> new Coordinates(x, y));
    }

    public Coordinates incrementX(){
        return of(x+1,y);
    }

    public Coordinates decrementX(){
        return of(x-1,y);
    }

    public Coordinates incrementY(){
        return of(x,y+1);
    }

    public Coordinates decrementY(){
        return of(x,y-1);
    }

    public Set<Coordinates> adjacent() {
        if (_adjacent == null) {
            _adjacent = Set.of(Coordinates.of(x - 1, y),
                    Coordinates.of(x - 1, y - 1),
                    Coordinates.of(x, y - 1),
                    Coordinates.of(x + 1, y - 1),
                    Coordinates.of(x + 1, y),
                    Coordinates.of(x + 1, y + 1),
                    Coordinates.of(x, y + 1),
                    Coordinates.of(x - 1, y + 1));
        }
        return _adjacent;
    }

    public double distance(Coordinates coordinates) {
        double xDiff = x - coordinates.x;
        xDiff *= xDiff;
        double yDiff = y - coordinates.y;
        yDiff *= yDiff;

        return Math.sqrt(xDiff + yDiff);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return _hash;
    }
}
