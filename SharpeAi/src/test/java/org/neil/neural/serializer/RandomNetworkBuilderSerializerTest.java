package org.neil.neural.serializer;

import org.junit.jupiter.api.Test;
import org.neil.neural.RandomNetworkBuilder;

import java.util.List;

public class RandomNetworkBuilderSerializerTest {

    @Test
    public void testSerializeThenDeserializeNetworkBuilder(){


        RandomNetworkBuilder rnb = new RandomNetworkBuilder(x->List.of(),x->List.of());

        RandomNetworkBuilder deserialized = SerializeTestHelper.serializeThenUnserialize(rnb);
    }
}
