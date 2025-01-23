package org.neil.neural;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.neil.util.RandomDoubleRange;

import org.neil.util.RandomIntRange;
import org.neil.util.RandomRatio;
import org.neil.util.mutator.Mutator;

import java.io.Serial;

@JsonTypeName("default")
@JsonDeserialize(as=MutateableNodeDefault.class)
public class MutateableNodeDefault extends AbstractNode implements MutateableNode{
    @Serial
    private static final long serialVersionUID = 3108793121583625752L;

    public MutateableNodeDefault(int id, int capacity, int activationLimit,double mutationRate) {
        super(id, capacity, activationLimit,mutationRate);
    }

    @JsonCreator()
    public MutateableNodeDefault(@JsonProperty("@id") int id,
                                 @JsonProperty("capacity") int capacity,
                                 @JsonProperty("stored") int stored,
                                 @JsonProperty("activateable") boolean activateable,
                                 @JsonProperty("activationLimit") int activationLimit,
                                 @JsonProperty("depreciate") int depreciate,
                                 @JsonProperty("mutationRate") double mutationRate) {
        super(id, capacity, stored, activateable, activationLimit, depreciate, mutationRate);
    }

    @Override
    public MutateableNode mutate(int capacityMin, int capacityMax, int activationMax) {
        int capacity = generateNewCapacity(capacityMin,capacityMax);
        int activation = generateNewActivation(capacity,activationMax);
        return new MutateableNodeDefault(this.getId(),
                capacity,
                activation,
                mutationRate());
    }

    @Override
    public Node copy() {
        return new MutateableNodeDefault(this.getId(),this.getCapacity(),this.getActivationLimit(),mutationRate());
    }


    public static DefaultMutator mutator(){
        return new DefaultMutator();
    }

    public static class DefaultMutator implements NodeMutator<MutateableNodeDefault>{

        @Serial
        private static final long serialVersionUID = 4408047468173402541L;

        @Override
        public MutateableNodeDefault generate(int id, int capacity) {
            return new MutateableNodeDefault(id,capacity,capacity/2,0);
        }
    }

    public static RandomBuilder builder(){
        return new RandomBuilder();
    }

    public static class RandomBuilder implements Mutator<MutateableNodeDefault,NewNodeInput> {

        @Serial
        private static final long serialVersionUID = 3576955957611187315L;

        private RandomIntRange capacityRange = new RandomIntRange(10,Integer.MAX_VALUE);
        private RandomRatio activationRatioRange = new RandomRatio(0.1f,1);
        private RandomDoubleRange mutationRate = new RandomDoubleRange(0.1,0.9);

        @Override
        public MutateableNodeDefault createMutatedCopy(double mutationRate, MutateableNodeDefault original, NewNodeInput input) {
            int newCapacity = capacityRange.mutateNumber(mutationRate,original.getCapacity());
            int activation = activationRatioRange.mutateRatio(mutationRate,newCapacity,original.getCapacity(),original.getActivationLimit());
            return new MutateableNodeDefault(original.getId(),
                    newCapacity,
                    activation,
                    this.mutationRate.mutateNumber(mutationRate,original.mutationRate()));
        }

        @Override
        public MutateableNodeDefault create(NewNodeInput input) {
            return null;
        }

        @Override
        public Class<MutateableNodeDefault> mutatorClass() {
            return MutateableNodeDefault.class;
        }
    }
}
