package org.neil.neural.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.neil.neural.AbstractNode;

import java.io.Serial;
import java.util.Random;


/**
 * An input node that will be filled randomly from zero to capacity inclusive
 *
 * @Author Neil Sharpe
 *
 */
public class RandomInputNode<E> extends AbstractNode implements InputNode<E> {
    private static final Random random = new Random();

    @Serial
    private final static long serialVersionUID = -3593584282714208032L;

    public RandomInputNode(int id,
                           int capacity,
                           @JsonProperty("mutationRate") double mutationRate) {
        super(id, capacity, 1,mutationRate);
    }

    @Override
    public void input(E toAdd) {
        try {
            clearStorage();
            addToStorage(random.nextInt(availableCapacity()));
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public InputNode copy() {
        return new RandomInputNode(getId(), getCapacity(),0);
    }
}
