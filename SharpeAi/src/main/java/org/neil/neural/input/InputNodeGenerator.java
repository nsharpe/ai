package org.neil.neural.input;

import java.io.Serializable;
import java.util.Collection;

public interface InputNodeGenerator<I> extends Serializable {
    Collection<InputNode<I>> inputs(int startingIndex);
}
