package org.neil.gameoflife;

import org.junit.jupiter.api.Test;
import org.neil.board.Coordinates;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameOfLifeRuleEngineTest {

    GameOfLifeRuleEngine engine = new GameOfLifeRuleEngine(3,3,2,2);

    @Test
    void notAdjacent() {
        Set<Coordinates> notAdjacent = Set.of(Coordinates.of(0,0), Coordinates.of(2,2));

        assertTrue(engine.apply(notAdjacent).isEmpty());
    }

    @Test
    void adjacentButNotEnoughToKeepAlive() {
        Set<Coordinates> adjacent = Set.of(Coordinates.of(0,0), Coordinates.of(1,1));

        assertTrue(engine.apply(adjacent).isEmpty());
    }

    @Test
    void birth() {
        Set<Coordinates> birthable = Set.of(Coordinates.of(0,0), Coordinates.of(0,2),Coordinates.of(2,2));

        assertEquals(1,engine.apply(birthable).size());
        assertTrue(engine.apply(birthable).contains(Coordinates.of(1,1)));
    }

    @Test
    void keepAlive() {
        Set<Coordinates> keepAlive = Set.of(Coordinates.of(0,0), Coordinates.of(1,1),Coordinates.of(2,2));

        assertEquals(1,engine.apply(keepAlive).size());
    }
}