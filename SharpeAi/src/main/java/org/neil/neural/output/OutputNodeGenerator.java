package org.neil.neural.output;

import java.util.Collection;

public interface OutputNodeGenerator {
    Collection<OutputNode> outputs(int startingIndex);
}
