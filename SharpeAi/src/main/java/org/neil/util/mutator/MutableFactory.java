package org.neil.util.mutator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neil.util.RandomRangeHelper;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.neil.util.RandomRangeHelper.*;

public class MutableFactory<T extends Mutatable,I> implements Serializable {

    @Serial
    private static final long serialVersionUID = -8272928161691239743L;


    @JsonIgnore
    private final Map<Class<? extends T>,Mutator<? extends T,I>> mutators = new HashMap<>();

    /**
     * This is duplicated from mutators so that a random mutator can be used on object creation without continual casting to lists
     */
    private final List<Mutator<? extends T,I>> mutatorList = new ArrayList<>();


    public MutableFactory<T,I> add(Mutator<? extends T,I> mutator){
        Objects.requireNonNull(mutator);
        mutators.put(mutator.mutatorClass(),mutator);
        mutatorList.add(mutator);
        return this;
    }

    public MutableFactory<T,I> addMutators(Collection<Mutator<? extends T,I>> mutators){
        mutators.forEach(this::add);
        return this;
    }

    public Map<Class<? extends T>, Mutator<? extends T, I>> getMutators() {
        return Collections.unmodifiableMap(mutators);
    }

    public MutableFactory<T,I> clear(){
        mutatorList.clear();
        mutators.clear();
        return this;
    }

    public MutableFactory<T,I> removeMutatorForClass(Class<? extends T> toRemove){
        mutatorList.remove(mutators.remove(toRemove));
        return this;
    }

    public  <ET extends T> Mutator<ET,I> getMutator(T type){
        return (Mutator<ET, I>) mutators.get(type.getClass());
    }

    /**
     * Creates a randomly generated entity based off a given input.
     *
     * The class and it's implementation will be random generated
     *
     * @param input
     * @return
     */
    public T build(I input) {
        return mutatorList.get(nextInt(0, mutatorList.size()))
                .create(input);
    }

    public T build(I input, List<Class<? extends T>> whiteList ) {
        return getRandomElement( whiteList )
                .map(mutators::get)
                .orElseThrow()
                .create(input);
    }

    public <ET extends T> T  mutate(double parentMutationRate, ET original, I input){
        double mutationRate = parentMutationRate * original.mutationRate();

        // Complete change the original
        if(RandomRangeHelper.mutate(mutationRate)){
            return build(input);
        }

        Mutator<ET,I> mutator = getMutator(original);
        if(mutator==null){
            throw new IllegalStateException(original.getClass()+":class not supported for mutations");
        }
        return mutator.createMutatedCopy(original.mutationRate(),
                original,
                input);
    }
}
