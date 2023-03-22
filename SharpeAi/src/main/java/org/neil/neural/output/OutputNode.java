package org.neil.neural.output;

import org.neil.neural.Node;

public interface OutputNode<E> extends Node {

    void consumeOutput(E e);

    OutputNode copy();
}
