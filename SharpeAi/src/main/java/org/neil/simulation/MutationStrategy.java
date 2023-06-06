package org.neil.simulation;

import org.neil.neural.RandomNetworkBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;
import org.neil.neural.RandomNetworkBuilder.MutationType;

public enum MutationStrategy {
    FIRST_CHILD_NO_MUTATIONS(allBut(
            MutationType.connectionWeights
    )),
    ALWAYS_ALLOW(allBut(Collections.singleton(MutationType.REBUILD)));

    private List<MutationType> mutationTypes;
    MutationStrategy(RandomNetworkBuilder.MutationType... mutationTypes){
        this.mutationTypes = List.of(mutationTypes);
    }

    MutationStrategy(Collection<MutationType> mutationTypes){
        this.mutationTypes = mutationTypes.stream().toList();
    }

    private static List<MutationType> allBut(Collection<MutationType> mutationTypes, MutationType... mutationTypes1){
        return Arrays.stream(MutationType.values())
                .filter( x -> !mutationTypes.contains(x))
                .filter( x -> !List.of(mutationTypes1).contains(x))
                .toList();
    }

    public List<RandomNetworkBuilder.MutationType> getMutationTypes() {
        return mutationTypes;
    }
}
