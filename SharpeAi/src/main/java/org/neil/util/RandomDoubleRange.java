package org.neil.util;

import java.io.Serial;
import java.io.Serializable;

public record RandomDoubleRange(double origin,
                                double bound) implements Serializable {

    @Serial
    private static final long serialVersionUID = 4532255181379368350L;


    public double nextFloat() {
        return RandomRangeHelper.nextFloat(origin, bound);
    }

    public double mutateNumber(double chanceToMutate,
                              double originalValue){
        if(RandomRangeHelper.nextFloat(0,1.0f) > chanceToMutate){
            return originalValue;
        }

        /**
         * Most useful mutations are minor changes to existing values instead of completely redoing a value.
         *
         * Thus mutating will favor minor changes based on the change to mutate ratio
         *
         * Thus the following line where the value is mutated with a range of originalValue +-3%
         */
        if(RandomRangeHelper.nextFloat(0,1.0f) > Math.max(1-chanceToMutate,0.8f)){
            return Math.min(bound,Math.max(origin,
                RandomRangeHelper.nextFloat(0.97f,1.03f) * originalValue));
        }
        return nextFloat();
    }
}
