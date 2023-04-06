package org.neil.simulation;

import org.neil.neural.NetworkOwner;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Tournament<I extends NetworkOwner, O> {

    private List<I> currentRanking;

    private Map<Integer, I> inputIds = new HashMap<>();
    private Map<I, Integer> winCount = new HashMap<>();
    private Map<I, Integer> stalemateCount = new HashMap<>();
    private Map<I, Integer> eloRanking = new HashMap<>();

    private void addInput(I input) {
        inputIds.put(inputIds.size() + 1, input);
        winCount.put(input, 0);
        stalemateCount.put(input, 0);
        eloRanking.put(input, 400);
    }

    private class game {
        final I firstPlayer;
        final I secondPlayer;

        /*
        If null then the game ended in a stalemate.  Otherwise winner is who won the game;
         */
        I winner;

        public game(I firstPlayer, I secondPlayer) {
            this.firstPlayer = firstPlayer;
            this.secondPlayer = secondPlayer;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            game game = (game) o;
            return Objects.equals(inputIds.get(firstPlayer),inputIds.get(game.firstPlayer))
                    && Objects.equals(inputIds.get(secondPlayer), inputIds.get(game.secondPlayer));
        }

        @Override
        public int hashCode() {
            return Objects.hash(inputIds.get(firstPlayer), inputIds.get(secondPlayer));
        }
    }

    private int rank(int o1, int o2) {
        if (winCount.get(o1) != winCount.get(o2)) {
            return winCount.get(o1) - winCount.get(o2);
        }
        return stalemateCount.get(o1) - stalemateCount.get(o2);
    }

}
