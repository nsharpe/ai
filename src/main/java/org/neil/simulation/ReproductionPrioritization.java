package org.neil.simulation;

import org.neil.map.Coordinates;
import org.neil.object.Creature;

import java.util.Comparator;

public class ReproductionPrioritization {

    public static Comparator<Creature> xCompare() {
        return Comparator.comparing(x -> x.getPosition().x);
    }

    public static Comparator<Creature> yCompare() {
        return Comparator.comparing(x -> x.getPosition().y);
    }

    public static Comparator<Creature> euclidianCompare(int x, int y) {
        Coordinates coordinates = Coordinates.of(x, y);
        return Comparator.comparing(creature -> creature.getPosition().distance(coordinates));
    }
}
