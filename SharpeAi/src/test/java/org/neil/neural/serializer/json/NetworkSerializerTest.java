package org.neil.neural.serializer.json;

import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.neil.neural.Connection;
import org.neil.neural.Network;
import org.neil.neural.Node;
import org.neil.neural.NodeDefault;
import org.neil.neural.RandomNetworkBuilder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NetworkSerializerTest {

    @Test
    public void networkSerializer() throws Exception {
        ObjectMapper om = new ObjectMapper();
        om.configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature(), true);

        String json = om.writeValueAsString(new RandomNetworkBuilder(x -> Collections.emptyList(),
                x -> Collections.emptyList())
                .maxNodes(1)
                .minNodes(1)
                .maxConnection(1)
                .build());

        System.out.println(json);

        // Does a spot check to see if nodes can be deserialized
        Network network = om.readValue(json, Network.class);
        assertTrue(network.getInputs().isEmpty());
        assertTrue(network.getOutputs().isEmpty());
        assertEquals(1,network.getIntermediateNodes().size());
        assertEquals(1, network.streamConnections().count());
        Connection connection = network.streamConnections().findFirst().orElse(null);
        if(connection == null){
            throw new NullPointerException();
        }
        assertEquals(connection.getDestination(),connection.getSource());
    }

    @Test
    public void networkOneConnectionOneNodeSerializer() throws Exception {
        ObjectMapper om = new ObjectMapper();

        Node node = new NodeDefault(1);
        Connection connection = new Connection(
                node,
                node,
                1,
                1.0,
                Connection.ConnectionType.ADD
        );

        om.configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature(), true);
        String json = om.writeValueAsString(new Network(List.of(),
                List.of(),
                List.of(node),
                List.of(connection)));

        System.out.println(json);

        // Does a spot check to see if nodes can be deserialized
        om.readValue(json, Network.class);
    }

    @Test
    public void nodeRandomTest() throws Exception {
        ObjectMapper om = new ObjectMapper();
        om.configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature(), true);
        List<Node> nodes = new RandomNetworkBuilder(x -> Collections.emptyList(), x -> Collections.emptyList())
                .maxNodes(101)
                .minNodes(100)
                .build().getIntermediateNodes();

        for (Node node : nodes) {
            String json = om.writeValueAsString(node);
            try {
                om.readValue(json, Node.class);
            } catch (Exception e) {
                System.out.println(node.getClass());
                throw e;
            }
        }
    }
}
