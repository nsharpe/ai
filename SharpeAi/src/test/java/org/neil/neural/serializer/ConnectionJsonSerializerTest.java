package org.neil.neural.serializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neil.neural.Connection;
import org.neil.neural.Node;
import org.neil.neural.NodeAlwaysEmpty;
import org.neil.neural.NodeAlwaysFull;
import org.neil.neural.NodeDefault;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ConnectionJsonSerializerTest {


    Node nodeDefault = new NodeDefault(1);
    Node alwaysFull = new NodeAlwaysFull(3);
    Node alwaysEmpty = new NodeAlwaysEmpty(5);

    ObjectMapper objectMapper;

    @BeforeEach
    public void before(){
        objectMapper = new ObjectMapper();
        objectMapper.configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature(),true);

    }

    @Test
    public void serializeConnection() throws Exception {

        int bandwith = 11;
        double mutiplier = 5.0f;

        Connection connection = new Connection(nodeDefault,
                alwaysEmpty,
                bandwith,
                mutiplier,
                Connection.ConnectionType.ADD);


        TestSerializer testSerializer = new TestSerializer(
                connection
        );

        System.out.println(objectMapper.writeValueAsString(testSerializer));

    }

    @Test
    public void serializerArrayList() throws Exception {

        Connection first = new Connection(new NodeDefault(1, 20, 100),
                new NodeDefault(2, 20, 100),
                3,
                5,
                Connection.ConnectionType.ADD);

        Connection second = new Connection(new NodeDefault(1, 10, 50),
                new NodeDefault(2, 30, 60),
                7,
                11,
                Connection.ConnectionType.ADD);

        TestSerializerCollection collection = new TestSerializerCollection();

        collection.listS = List.of(
                new TestSerializer(first),
                new TestSerializer(second)
        );

        String json = objectMapper.writeValueAsString(collection);

        System.out.println(json);

        assertEquals("{\"listS\":[{\"input\":{\"type\":\"default\",\"id\":1,\"capacity\":20,\"stored\":0,\"activateable\":false,\"activationLimit\":100,\"depreciate\":20},\"output\":{\"type\":\"default\",\"id\":2,\"capacity\":20,\"stored\":0,\"activateable\":false,\"activationLimit\":100,\"depreciate\":20},\"connection\":{\"source\":1,\"destination\":2,\"bandwidth\":3,\"multiplier\":5.0,\"connectionType\":\"ADD\"}},{\"input\":{\"type\":\"default\",\"id\":1,\"capacity\":10,\"stored\":0,\"activateable\":false,\"activationLimit\":50,\"depreciate\":10},\"output\":{\"type\":\"default\",\"id\":2,\"capacity\":30,\"stored\":0,\"activateable\":false,\"activationLimit\":60,\"depreciate\":12},\"connection\":{\"source\":1,\"destination\":2,\"bandwidth\":7,\"multiplier\":11.0,\"connectionType\":\"ADD\"}}]}",
                json);


    }

    @Test
    public void deserializeConnectionArray() throws Exception{
        String json = "{\"listS\":[{\"input\":{\"type\":\"default\",\"id\":1,\"capacity\":20,\"stored\":0,\"activateable\":false,\"activationLimit\":100,\"depreciate\":20},\"output\":{\"type\":\"default\",\"id\":2,\"capacity\":20,\"stored\":0,\"activateable\":false,\"activationLimit\":100,\"depreciate\":20},\"connection\":{\"source\":1,\"destination\":2,\"bandwidth\":3,\"multiplier\":5.0,\"connectionType\":\"ADD\"}},{\"input\":{\"type\":\"default\",\"id\":1,\"capacity\":10,\"stored\":0,\"activateable\":false,\"activationLimit\":50,\"depreciate\":10},\"output\":{\"type\":\"default\",\"id\":2,\"capacity\":30,\"stored\":0,\"activateable\":false,\"activationLimit\":60,\"depreciate\":12},\"connection\":{\"source\":1,\"destination\":2,\"bandwidth\":7,\"multiplier\":11.0,\"connectionType\":\"ADD\"}}]}";

        TestSerializerCollection testSerializers = objectMapper.readValue(json,TestSerializerCollection.class);

        assertEquals(2,testSerializers.listS.size());

        TestSerializer first = testSerializers.listS.get(0);
        assertEquals(first.connection.getSource(),first.input);
        assertEquals(first.connection.getDestination(),first.output);

        TestSerializer second = testSerializers.listS.get(1);
        assertEquals(second.connection.getSource(),second.input);
        assertEquals(second.connection.getDestination(),second.output);
    }

    private static class TestSerializerCollection{
        @JsonProperty
        List<TestSerializer> listS = new ArrayList<>();
    }

    private static class TestSerializer {

        @JsonProperty
        Node input;
        @JsonProperty
        Node output;
        @JsonProperty
        Connection connection;

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
