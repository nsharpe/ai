package org.neil.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Coordinates {
    public final int x;
    public final int y;
    private int _hash;

    private static Map<Integer,Map<Integer,Coordinates>> cache = new HashMap<>();

    private Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
        this._hash = Objects.hash(x, y);
    }

    public static Coordinates of(final int x, final int y){
        return cache.computeIfAbsent(x,(key) -> new HashMap<>())
                .computeIfAbsent(y, key -> new Coordinates(x,y));
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
