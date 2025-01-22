package org.neil.util.mutator;

import java.io.Serializable;

public interface Mutator<T,I> extends Serializable {

    T createMutatedCopy(float mutationRate, T original, I input);

    T create(I input);

    Class<T> mutatorClass();
}