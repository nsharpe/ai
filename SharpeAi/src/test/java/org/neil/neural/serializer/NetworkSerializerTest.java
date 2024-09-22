package org.neil.neural.serializer;

import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.neil.neural.Network;
import org.neil.neural.Node;
import org.neil.neural.RandomNetworkBuilder;

import java.util.Collections;
import java.util.List;

public class NetworkSerializerTest {

    @Test
    public void networkSerializer()throws Exception{
        ObjectMapper om = new ObjectMapper();
        om.configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature(),true);
        String json = om.writeValueAsString(new RandomNetworkBuilder(x->Collections.emptyList(),x->Collections.emptyList())
                    .maxNodes(101)
                        .minNodes(100)
                .build());

        System.out.println(json);

        // Does a spot check to see if nodes can be deserialized
        om.readValue(json,Network.class);
    }

    @Test
    public void nodeRandomTest() throws Exception{
        ObjectMapper om = new ObjectMapper();
        om.configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature(),true);
        List<Node> nodes = new RandomNetworkBuilder(x->Collections.emptyList(),x->Collections.emptyList())
                .maxNodes(101)
                .minNodes(100)
                .build().getIntermediateNodes();

        for(Node node: nodes){
            String json = om.writeValueAsString(node);
            try{
                om.readValue(json,Node.class);
            }catch (Exception e){
                System.out.println(node.getClass());
                throw e;
            }
        }
    }
}
