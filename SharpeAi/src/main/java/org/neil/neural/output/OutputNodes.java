package org.neil.neural.output;

import org.neil.neural.Output;

import java.util.Collection;

public interface OutputNodes {
    Collection<Output> outputs(int startingIndex);
}
