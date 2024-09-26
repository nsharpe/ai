package org.neil.neural.output;

import java.io.Serializable;
import java.util.Collection;

public interface OutputNodeGenerator<O> extends Serializable {
    Collection<OutputNode<O>> outputs(int startingIndex);
}
