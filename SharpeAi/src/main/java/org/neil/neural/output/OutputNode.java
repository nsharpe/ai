package org.neil.neural.output;

import org.neil.neural.Node;
import org.neil.object.Creature;

public interface OutputNode extends Node {

    void consumeOutput(Creature creatureConsumer);

    OutputNode copy();
}
