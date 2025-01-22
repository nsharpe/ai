package org.neil.util;



import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The purpose of this class is to provide wide changes to how random numbers are handled in a central location
 * In theory this class is completely unnecessary
 */
public class RandomRangeHelper implements Serializable {
    @Serial
    private static final long serialVersionUID = 2365009739870931668L;

    private RandomRangeHelper(){
        //noop
    }

    public static boolean nextBoolean(){
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static boolean mutateBoolean(float mr, boolean original){
        if(nextFloat() < mr){
            return original;
        }
        return nextBoolean();
    }

    public static short nextShort(){
        return (short)ThreadLocalRandom.current().nextInt(Short.MIN_VALUE,Short.MAX_VALUE);
    }

    public static double nextFloat(double min, double max){
        return ThreadLocalRandom.current().nextDouble(min,max);
    }

    public static float nextFloat(){
        return ThreadLocalRandom.current().nextFloat();
    }

    public static float nextFloat(float min, float max){
        return ThreadLocalRandom.current().nextFloat(min,max);
    }

    public static int nextInt(int origin, int bound){
        return ThreadLocalRandom.current().nextInt(origin,bound);
    }

    public static <X> X getRandomElement(X[] items){
        return items[ThreadLocalRandom.current().nextInt(items.length)];
    }

    public static <X> X getRandomElement(float mutationRate, X original, X[] items){
        if(ThreadLocalRandom.current().nextFloat() > mutationRate){
            return original;
        }
        return items[ThreadLocalRandom.current().nextInt(items.length)];
    }

    public static <X> Optional<X> getRandomElement(List<X> items){
        if(items.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(items.get(nextInt(0,items.size())));
    }

    public static <X> Optional<X> getRandomElementOrNone(List<X> items){
        if(items.isEmpty()){
            return Optional.empty();
        }
        int next = nextInt(0,items.size());
        return Optional.of(items.get(next));
    }

    public static <X> Optional<X> getRandomElement(double mutationRate, Optional<X> original, List<X> items){
        if(ThreadLocalRandom.current().nextDouble() > mutationRate){
            return original;
        }
        return getRandomElement(items);
    }

    public static <X> Optional<X> getRandomElement(float mutationRate, Optional<X> original, List<X> items){
        if(ThreadLocalRandom.current().nextFloat() > mutationRate){
            return original;
        }
        return getRandomElement(items);
    }

    public static <X> Optional<X> getRandomElementOrNone(double mutationRate, Optional<X> original, List<X> items){
        if(ThreadLocalRandom.current().nextDouble() > mutationRate){
            return original;
        }
        return getRandomElementOrNone(items);
    }

    public static <X> Optional<X> getRandomElementOrNone(float mutationRate, Optional<X> original, List<X> items){
        if(ThreadLocalRandom.current().nextFloat() > mutationRate){
            return original;
        }
        return getRandomElementOrNone(items);
    }
}
