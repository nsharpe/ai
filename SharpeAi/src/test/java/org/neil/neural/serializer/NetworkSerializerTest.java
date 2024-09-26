package org.neil.neural.serializer;

import org.junit.jupiter.api.Test;
import org.neil.neural.Connection;
import org.neil.neural.Network;
import org.neil.neural.Node;
import org.neil.neural.RandomNetworkBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class NetworkSerializerTest {

    @Test
    public void networkSerializer() throws Exception {

        Network original = new RandomNetworkBuilder(x -> Collections.emptyList(),
                x -> Collections.emptyList())
                .maxNodes(1)
                .minNodes(1)
                .maxConnection(1)
                .build();

        Object obj = serializeThenDeserialize(original);

        assertNotNull(obj);

        // Does a spot check to see if nodes can be deserialized
        Network network = assertInstanceOf(Network.class,obj);

        assertTrue(network.getInputs().isEmpty());
        assertTrue(network.getOutputs().isEmpty());
        assertEquals(1,network.getIntermediateNodes().size());
        assertEquals(1, network.streamConnections().count());
        Connection connection = network.streamConnections().findFirst().orElse(null);
        if(connection == null){
            throw new NullPointerException();
        }
        assertSame(connection.getDestination(),connection.getSource());
        assertSame(connection.getDestination(),network.getIntermediateNodes().stream().findAny().orElseThrow());
        assertNotSame(connection.getDestination(), original.getIntermediateNodes().stream().findFirst().orElseThrow());
    }

    @Test
    public void largeNetworkSerializer() throws Exception {
        //The goal is to try as many

        Network original = new RandomNetworkBuilder(x -> Collections.emptyList(),
                x -> Collections.emptyList())
                .maxNodes(1000)
                .minNodes(1000)
                .maxConnection(1000)
                .build();

        Object obj = serializeThenDeserialize(original);

        assertNotNull(obj);

        // Does a spot check to see if nodes can be deserialized
        Network network = assertInstanceOf(Network.class,obj);

        Map<Integer,Node> originalNodes = original.getIntermediateNodes().stream().collect(
                Collectors.toMap(Node::getId,x->x)
        );

        for(Node node: network.getIntermediateNodes()){
           Node originalNode = originalNodes.get(node.getId());
           assertNotNull(originalNode);
           assertNotSame(originalNode,node);
           assertInstanceOf(originalNode.getClass(),node);
        }
    }

    private static Object serializeThenDeserialize(Object obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(obj);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        return objectInputStream.readObject();
    }
}
