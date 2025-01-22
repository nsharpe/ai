package org.neil.util;

import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class RandomHelper implements Serializable {
    @Serial
    private static final long serialVersionUID = 2365009739870931668L;

    @JsonIgnore
    private Vector2 mapDimensions;

    public RandomHelper(@JsonProperty("width") float width,
                        @JsonProperty("height") float height){
        this(new Vector2(width,height));
    }

    public RandomHelper(Vector2 mapDimensions) {
        this.mapDimensions = Objects.requireNonNull(mapDimensions);
    }

    public float getWidth(){
        return mapDimensions.x;
    }

    public float getHeight(){
        return mapDimensions.y;
    }


    public Vector2 randomPosition(){
        float x = mapDimensions.x;
        float y = mapDimensions.y;
        return new Vector2(ThreadLocalRandom.current().nextFloat(-x,x),
            ThreadLocalRandom.current().nextFloat(-y,y));
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
        int next = nextInt(0,items.size()+1);
        if(next>=items.size()){
            return Optional.empty();
        }
        return Optional.of(items.get(next));
    }

    public static <X> Optional<X> getRandomElement(float mutationRate, Optional<X> original, List<X> items){
        if(ThreadLocalRandom.current().nextFloat() > mutationRate){
            return original;
        }
        return getRandomElement(items);
    }

    public static <X> Optional<X> getRandomElementOrNone(float mutationRate, Optional<X> original, List<X> items){
        if(ThreadLocalRandom.current().nextFloat() > mutationRate){
            return original;
        }
        return getRandomElementOrNone(items);
    }
}
