package org.neil.neural.input;

import java.util.Collection;

public interface InputNodeGenerator<I> {
    Collection<InputNode<I>> inputs(int startingIndex);
}
