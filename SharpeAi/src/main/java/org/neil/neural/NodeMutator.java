package org.neil.neural;

import java.io.Serializable;

public interface NodeMutator <T extends Node> extends Serializable {

    T generate(int id, int capacity);
}
