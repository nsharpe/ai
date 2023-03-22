package org.neil.neural.input;

import org.neil.neural.Input;

import java.util.Collection;

public interface InputNodes {
    Collection<Input> inputs(int startingIndex);
}
