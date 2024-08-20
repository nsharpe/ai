package org.neil.gameoflife;

import org.neil.board.Coordinates;

import java.util.Set;
import java.util.stream.Collectors;

public record GameOfLifeRuleEngine(int birthMin,
                                   int birthMax,
                                   int keepAliveMin,
                                   int keepAliveMax){

    public GameOfLifeRuleEngine {
        if(birthMax<birthMin){
            throw new IllegalStateException("birthMax<birthMin");
        }

        if(keepAliveMax< keepAliveMin){
            throw new IllegalStateException("keepAliveMax<keepAliveMin");
        }
    }

    public Set<Coordinates> apply(Set<Coordinates> coordinates){
        return coordinates.stream()
                .flatMap(x->x.adjacent().stream())
                .collect(Collectors.groupingBy(x->x,Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> isBirthable(entry.getValue()) || staysAlive(entry.getValue(),entry.getKey(),coordinates))
                .map(x-> x.getKey())
                .collect(Collectors.toSet());
    }

    private boolean isBirthable(Long count){
        return count >= birthMin && count <= birthMax;
    }

    private boolean staysAlive(Long count,Coordinates coordinates, Set<Coordinates> currentlyAlive){
        if(!currentlyAlive.contains(coordinates)){
            return false;
        }
        return count >= keepAliveMin && count <= keepAliveMax;
    }
}
