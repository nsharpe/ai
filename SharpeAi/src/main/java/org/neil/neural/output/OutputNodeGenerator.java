package org.neil.neural.output;

import java.util.Collection;

public interface OutputNodeGenerator<O> {
    Collection<OutputNode<O>> outputs(int startingIndex);
}
