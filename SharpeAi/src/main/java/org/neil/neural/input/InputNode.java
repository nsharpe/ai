package org.neil.neural.input;

import org.neil.neural.Node;

public interface InputNode<E> extends Node {

    void input(E input);

    InputNode copy();
}
