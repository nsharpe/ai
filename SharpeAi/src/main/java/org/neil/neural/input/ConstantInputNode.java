package org.neil.neural.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.neil.neural.AbstractNode;

import java.io.Serial;

/**
 * An input node that at the start of a tick for a neural network fills to capacity
 *
 * @Author Neil Sharpe
 *
 */
public class ConstantInputNode<E> extends AbstractNode implements InputNode<E> {
    @Serial
    private final static long serialVersionUID = 2678688287496952476L;

    public ConstantInputNode(int id,
                             int capacity,
                             @JsonProperty("mutationRate") double mutationRate) {
        super(id, capacity, capacity / 2,mutationRate);
    }

    @Override
    public void input(E toAdd) {
        try {
            clearStorage();
            addToStorage(availableCapacity());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int availableCapacity() {
        return getCapacity();
    }

    @Override
    public void addToStorage(int toAdd) {
        fillStorage();
    }

    @Override
    public InputNode copy() {
        return new ConstantInputNode(getId(), getCapacity(),0);
    }
}
