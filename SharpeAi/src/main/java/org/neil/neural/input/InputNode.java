package org.neil.neural.input;

import org.neil.neural.Node;

public interface InputNode extends Node {

    void input(Inputs toAdd);

    InputNode copy();
}
