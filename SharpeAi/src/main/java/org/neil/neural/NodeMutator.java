package org.neil.neural;

import java.io.Serializable;

public interface NodeMutator <N extends Node> extends Serializable {

    N generate(int id, int capacity);
}
