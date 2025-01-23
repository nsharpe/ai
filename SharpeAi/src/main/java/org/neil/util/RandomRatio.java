package org.neil.util;

import static org.neil.util.RandomRangeHelper.mutate;
import static org.neil.util.RandomRangeHelper.nextFloat;

public record RandomRatio(float origin, float bound) {


    public int mutateRatio(double mutationRate, int source, int originalSource, int originalValue){
        if(mutate(mutationRate)) {
            return (int) (source * nextFloat(origin, bound));
        }

        if(source == originalSource) {
            return originalValue;
        }

        double ratio = ((double) originalValue) / ((double) originalSource);

        return (int) (((double)source) * ratio);
    }

    public int nextRatio(int source){
        return (int)(source * nextFloat(origin,bound));
    }
}
