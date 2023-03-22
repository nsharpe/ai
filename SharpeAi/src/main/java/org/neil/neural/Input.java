package org.neil.neural;

import org.neil.neural.input.Inputs;

public interface Input extends Node{

    void input(Inputs toAdd);

    Input copy();
}
