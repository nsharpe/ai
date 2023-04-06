package org.neil.object;

import org.neil.board.Coordinates;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.neil.object.XDirection.EAST;
import static org.neil.object.XDirection.WEST;
import static org.neil.object.YDirection.NORTH;
import static org.neil.object.YDirection.SOUTH;

public enum Direction {
    NORTH_EAST(NORTH, EAST),
    NORTH_NEUTRAL(NORTH, XDirection.NEUTRAL),
    NORTH_WEST(NORTH, WEST),
    NEUTRAL_WEST(YDirection.NEUTRAL, WEST),
    SOUTH_WEST(SOUTH, WEST),
    SOUTH_NEUTRAL(SOUTH, XDirection.NEUTRAL),
    SOUTH_EAST(SOUTH, EAST),
    NEUTRAL_EAST(YDirection.NEUTRAL, EAST);

    private static Random random = new Random();


    public final XDirection xDirection;
    public final YDirection yDirection;
    private static Map<Direction, Direction> rightDirection = new HashMap<>();

    static {
        for (Direction direction : Direction.values()) {
            rightDirection.put(direction.left(), direction);
        }
    }

    Direction(YDirection yDirection, XDirection xDirection) {
        this.xDirection = xDirection;
        this.yDirection = yDirection;
    }

    public Direction left() {
        switch (this) {
            case NORTH_EAST:
                return NORTH_NEUTRAL;
            case NORTH_NEUTRAL:
                return NORTH_WEST;
            case NORTH_WEST:
                return NEUTRAL_WEST;
            case NEUTRAL_WEST:
                return SOUTH_WEST;
            case SOUTH_WEST:
                return SOUTH_NEUTRAL;
            case SOUTH_NEUTRAL:
                return SOUTH_EAST;
            case SOUTH_EAST:
                return NEUTRAL_EAST;
            case NEUTRAL_EAST:
                return NORTH_EAST;
            default:
                throw new IllegalStateException("Direction left function broken");
        }
    }

    public Direction right() {
        return rightDirection.get(this);
    }

    public static Direction random() {
        return Direction.values()[random.nextInt(Direction.values().length)];
    }

    public Coordinates position(Coordinates coordinates){
        int x = coordinates.x;
        int y = coordinates.y;

        switch (this.xDirection){
            case EAST -> x++;
            case WEST -> x--;
        }

        switch (this.yDirection){
            case NORTH -> y++;
            case SOUTH -> y--;
        }

        return Coordinates.of(x,y);
    }
}
