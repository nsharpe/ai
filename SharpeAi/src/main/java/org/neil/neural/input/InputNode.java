package org.neil.neural.input;

import org.neil.neural.Node;


/**
 *
 * Allows for input into a neural network.
 *
 * All input nodes for a given neural network will receive the same input class.  It is up to the implementation of this
 * class to determine how to parse the input into a tuple that can be understood by the network.
 *
 * For example if the input was a Vehicle class there could be attributes such as type, and mileage.  There should be
 * two implementations of InputNode one for type and mileage.  These inputs should be returned via the
 * InputNodeGenerator class.
 *
 * @Author Neil Sharpe
 *
 */
public interface InputNode<E> extends Node {

    void input(E input);

    InputNode copy();
}
