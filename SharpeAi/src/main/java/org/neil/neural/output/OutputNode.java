package org.neil.neural.output;

import org.neil.neural.Node;
import org.neil.object.Creature;

public interface OutputNode<E> extends Node {

    void consumeOutput(E creatureConsumer);

    OutputNode copy();
}
