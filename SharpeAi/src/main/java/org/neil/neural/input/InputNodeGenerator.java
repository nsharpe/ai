package org.neil.neural.input;

import java.util.Collection;

public interface InputNodeGenerator {
    Collection<InputNode> inputs(int startingIndex);
}
