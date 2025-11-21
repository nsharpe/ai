package org.neil.simulation;

import org.neil.neural.RandomNetworkBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.neil.neural.RandomNetworkBuilder.MutationType;

public enum MutationStrategy {
    NONE(Collections.emptyList()),
    WEIGHTS_ONLY(allBut(
            MutationType.connectionWeights
    )),
    REMOVE_ONLY(MutationType.NODE_REMOVAL,
            MutationType.CONNECTION_REMOVAL),
    ADD_ONLY(MutationType.CONNECTION_ADD,
            MutationType.CONNECTION_TO_NODE),
    MODIFY_WEIGHTS(MutationType.CONNECTION_STRENGTH,
            MutationType.NODE_RANDOMIZE_CAPACITY),
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
