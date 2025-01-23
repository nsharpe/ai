package org.neil.neural.serializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.junit.jupiter.api.Test;
import org.neil.neural.Connection;
import org.neil.neural.MutateableNodeDefault;
import org.neil.neural.Node;
import org.neil.neural.NodeAlwaysEmpty;
import org.neil.neural.NodeAlwaysFull;
import org.neil.neural.AbstractNode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ConnectionJsonSerializerTest {


    Node nodeDefault = new MutateableNodeDefault(1,100,50,0);
    Node alwaysFull = new NodeAlwaysFull(3,7.0);
    Node alwaysEmpty = new NodeAlwaysEmpty(5,0.7);

    @Test
    public void serializeConnection() throws Exception {

        int bandwith = 11;
        double mutiplier = 5.0f;

        Connection connection = new Connection(nodeDefault,
                alwaysEmpty,
                bandwith,
                mutiplier,
                Connection.ConnectionType.ADD);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(connection);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        Object obj = objectInputStream.readObject();

        assertNotNull(obj);
        Connection deserialized = assertInstanceOf(Connection.class,obj);
        assertEquals(connection.getDestination().getId(),deserialized.getDestination().getId());

    }

    @Test
    public void serializeConnectionSameNode() throws Exception {

        int bandwith = 11;
        double mutiplier = 5.0f;

        Connection connection = new Connection(nodeDefault,
                nodeDefault,
                bandwith,
                mutiplier,
                Connection.ConnectionType.ADD);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(connection);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        Object obj = objectInputStream.readObject();

        assertNotNull(obj);
        Connection deserialized = assertInstanceOf(Connection.class,obj);
        assertEquals(deserialized.getDestination(),deserialized.getSource());

        assertTrue(deserialized.getDestination()==deserialized.getSource());
    }

    @Test
    public void serializerArrayList() throws Exception {

        Connection first = new Connection(new MutateableNodeDefault(1, 20, 100,0),
                new MutateableNodeDefault(2, 20, 100,0),
                3,
                5,
                Connection.ConnectionType.ADD);

        Connection second = new Connection(new MutateableNodeDefault(1, 10, 50,0),
                new MutateableNodeDefault(2, 30, 60,0),
                7,
                11,
                Connection.ConnectionType.ADD);

        List<TestSerializer> collection = List.of(
                new TestSerializer(first),
                new TestSerializer(second)
        );

        new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(collection);
    }


    @JsonTypeName("testSerializerConnection")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class TestSerializer implements Serializable {

        @JsonProperty
        Node input;
        @JsonProperty
        Node output;
        @JsonProperty
        Connection connection;

        @JsonCreator
        public TestSerializer(@JsonProperty("input") Node input,
                              @JsonProperty("output") Node output,
                              @JsonProperty("connection") Connection connection) {
            this.input = input;
            this.output = output;
            this.connection = connection;
        }

        public TestSerializer(Connection connection) {
            this.connection = connection;
            this.input = connection.getSource();
            this.output = connection.getDestination();
        }

        public Node getInput() {
            return input;
        }

        public Node getOutput() {
            return output;
        }

        public Connection getConnection() {
            return connection;
        }
    }
}
