package org.neil.neural;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serial;

@JsonTypeName("alwaysFull")
@JsonDeserialize(as= NodeAlwaysFull.class)
public class NodeAlwaysFull extends AbstractNode {

    @Serial
    private final static long serialVersionUID = -8797062803984779965L;

    public NodeAlwaysFull(int id) {
        super(id);
    }

    public NodeAlwaysFull(int id, int capacity) {
        super(id, capacity, capacity / 2);
        super.addToStorage(capacity);
    }

    @JsonCreator
    public NodeAlwaysFull(@JsonProperty("@id") int id,
                          @JsonProperty("capacity")int capacity,
                          @JsonProperty("stored")int stored,
                          @JsonProperty("activateable") boolean activateable,
                          @JsonProperty("activationLimit") int activationLimit,
                          @JsonProperty("depreciate") int depreciate) {
        super(id, capacity, stored, activateable, activationLimit, depreciate);
    }

    public NodeAlwaysFull(Node node) {
        super(node);
    }

    @Override
    public void addToStorage(int toAdd) {
        // noop
    }

    @Override
    public Node copy() {
        return new NodeAlwaysFull(this);
    }

    public static Mutator mutator(){
        return new Mutator();
    }

    public static class Mutator implements NodeMutator<NodeAlwaysFull>{

        @Serial
        private static final long serialVersionUID = 3541297304868683669L;

        @Override
        public NodeAlwaysFull generate(int id, int capacity) {
            return new NodeAlwaysFull(id,capacity);
        }
    }
}
