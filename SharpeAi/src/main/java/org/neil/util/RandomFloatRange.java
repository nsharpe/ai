package org.neil.util;

import java.io.Serial;
import java.io.Serializable;

public record RandomFloatRange(float origin,
                               float bound) implements Serializable {

    @Serial
    private static final long serialVersionUID = 4532255181379368350L;


    public float nextFloat() {
        return RandomHelper.nextFloat(origin, bound);
    }

    public float mutateNumber(float chanceToMutate,
                              float originalValue){
        if(RandomHelper.nextFloat(0,1.0f) > chanceToMutate){
            return originalValue;
        }

        /**
         * Most useful mutations are minor changes to existing values instead of completely redoing a value.
         *
         * Thus mutating will favor minor changes based on the change to mutate ratio
         *
         * Thus the following line where the value is mutated with a range of originalValue +-3%
         */
        if(RandomHelper.nextFloat(0,1.0f) > Math.max(1-chanceToMutate,0.8f)){
            return Math.min(bound,Math.max(origin,
                RandomHelper.nextFloat(0.97f,1.03f) * originalValue));
        }
        return nextFloat();
    }
}
