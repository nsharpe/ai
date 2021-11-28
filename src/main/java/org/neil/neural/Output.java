package org.neil.neural;

import org.neil.object.Creature;

public interface Output extends Node {

    void consumeOutput(Creature creatureConsumer);

    Output copy();
}
