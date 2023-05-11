package org.neil.simulation;

import org.neil.board.Coordinates;
import org.neil.object.Creature;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Supplier;

public class ReproductionPrioritization {

    public static Comparator<Creature> xCompare() {
        return Comparator.comparing(x -> x.getPosition().x);
    }

    public static Comparator<Creature> yCompare() {
        return Comparator.comparing(x -> x.getPosition().y);
    }

    public static Comparator<Creature> yCompare(int y) {
        return  Comparator.comparing( pos -> Math.abs(y-pos.getPosition().y));
    }

    public static Comparator<Creature> xCompare(int x) {

        return Comparator.comparing( pos -> Math.abs(x-pos.getPosition().x));
    }

    public static Comparator<Creature> closestToStart() {

        return Comparator.comparing(x-> x.getPosition().distance(x.getStartPosition()));
    }

    public static Comparator<Creature> euclidianCompare(Supplier<Coordinates> coordinates) {
        return euclidianCompare(coordinates.get());
    }

    public static Comparator<Creature> euclidianCompare(Coordinates coordinates) {
        return euclidianCompare(coordinates.x,coordinates.y);
    }
    public static Comparator<Creature> euclidianCompare(int x, int y) {
        Coordinates coordinates = Coordinates.of(x, y);
        return Comparator.comparing(creature -> creature.getPosition().distance(coordinates));
    }

    public static Comparator<Creature> compareChangesOnDiffRun(Simulation simulation,
                                                               Map<Integer, Comparator<Creature>> compareOnRunId) {
        return new ComparatorChanger(simulation,compareOnRunId);
    }

    private static class ComparatorChanger implements Comparator<Creature> {
        private final Simulation simulation;
        private Comparator<Creature> currentCompare = (x, y) -> 0;
        private final Map<Integer, Comparator<Creature>> compareOnRunId;

        public ComparatorChanger(Simulation simulation,
                                 Map<Integer, Comparator<Creature>> compareOnRunId) {
            this.simulation = simulation;
            this.compareOnRunId = compareOnRunId;
        }

        public int compare(Creature o1, Creature o2) {
            currentCompare = compareOnRunId.getOrDefault(simulation.getRunsCompleted(), currentCompare);

            return currentCompare.compare(o1, o2);
        }
    }

}
