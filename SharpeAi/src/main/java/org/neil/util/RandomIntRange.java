package org.neil.util;

import java.io.Serial;
import java.io.Serializable;

public record RandomIntRange(int origin,
                             int bound) implements Serializable {

    @Serial
    private static final long serialVersionUID = 8652833448039291851L;

    public int next() {
        return RandomRangeHelper.nextInt(origin, bound);
    }

    public int mutateNumber(float chanceToMutate,
                              int originalValue){
        if(RandomRangeHelper.nextFloat(0,1.0f) > chanceToMutate){
            return originalValue;
        }
        if(RandomRangeHelper.nextFloat(0,1.0f) > chanceToMutate){

            if(bound-origin < 100){
                return bound(RandomRangeHelper.nextInt(originalValue-1,originalValue+2));
            }

            return bound(
                (int)(originalValue + ((float)originalValue * RandomRangeHelper.nextFloat(0.99f,1.01f)) ));
        }
        return next();
    }

    private int bound(int newValue){
        return Math.min(bound,Math.max(origin,newValue));
    }
}
