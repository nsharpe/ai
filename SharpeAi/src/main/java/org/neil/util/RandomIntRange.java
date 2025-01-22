package org.neil.util;

import java.io.Serial;
import java.io.Serializable;

public record RandomIntRange(int origin,
                             int bound) implements Serializable {

    @Serial
    private static final long serialVersionUID = 8652833448039291851L;

    public int next() {
        return RandomHelper.nextInt(origin, bound);
    }

    public int mutateNumber(float chanceToMutate,
                              int originalValue){
        if(RandomHelper.nextFloat(0,1.0f) > chanceToMutate){
            return originalValue;
        }
        if(RandomHelper.nextFloat(0,1.0f) > chanceToMutate){

            if(bound-origin < 100){
                return bound(RandomHelper.nextInt(originalValue-1,originalValue+2));
            }

            return bound(
                (int)(originalValue + ((float)originalValue * RandomHelper.nextFloat(0.99f,1.01f)) ));
        }
        return next();
    }

    private int bound(int newValue){
        return Math.min(bound,Math.max(origin,newValue));
    }
}
