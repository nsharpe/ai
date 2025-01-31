package org.neil.neural.serializer.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neil.neural.Connection;
import org.neil.neural.MutateableNodeDefault;
import org.neil.neural.Node;
import org.neil.neural.NodeAlwaysEmpty;
import org.neil.neural.NodeAlwaysFull;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class ConnectionJsonSerializerTest {


    Node nodeDefault = new MutateableNodeDefault(1,100,50,0);
    Node alwaysFull = new NodeAlwaysFull(3,0.7);
    Node alwaysEmpty = new NodeAlwaysEmpty(5,0.7);

    ObjectMapper objectMapper;

    @BeforeEach
    public void before() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        objectMapper.configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature(), true);

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

        String json = objectMapper.writeValueAsString(collection);

        System.out.println(json);

        assertEquals("[{\"connection\":{\"bandwidth\":3,\"connectionType\":\"ADD\",\"destination\":2,\"multiplier\":5.0,\"source\":1},\"input\":{\"type\":\"default\",\"@id\":1,\"activateable\":false,\"activationLimit\":100,\"capacity\":20,\"depreciate\":20,\"mutationRate\":0.0,\"stored\":0},\"output\":{\"type\":\"default\",\"@id\":2,\"activateable\":false,\"activationLimit\":100,\"capacity\":20,\"depreciate\":20,\"mutationRate\":0.0,\"stored\":0}},{\"connection\":{\"bandwidth\":7,\"connectionType\":\"ADD\",\"destination\":2,\"multiplier\":11.0,\"source\":1},\"input\":{\"type\":\"default\",\"@id\":1,\"activateable\":false,\"activationLimit\":50,\"capacity\":10,\"depreciate\":10,\"mutationRate\":0.0,\"stored\":0},\"output\":{\"type\":\"default\",\"@id\":2,\"activateable\":false,\"activationLimit\":60,\"capacity\":30,\"depreciate\":12,\"mutationRate\":0.0,\"stored\":0}}]",
                json);


    }

    @Test
    public void deserializeConnectionArray() throws Exception {
        Node input = new MutateableNodeDefault(1,100,50,0);
        Connection connection = new Connection(input,
                input,
                1,
                1,
                Connection.ConnectionType.SUBTRACT);
        String json = objectMapper.writeValueAsString(new TestSerializer(connection));

        System.out.println(json);

        try {
            Connection deserialized = objectMapper.readValue(json, Connection.class);
            // Currently connections can only be deserialized within the context of the Network class.
            // This is to handle circular references.  See NetworkDeserializer
            fail();
        } catch (Exception e) {
            // Exception expected
        }
    }


    @Test
    public void deserializeSingleConnection() throws Exception {
        String json = "{\"connection\":{\"bandwidth\":3,\"connectionType\":\"ADD\",\"destination\":2,\"multiplier\":5.0,\"source\":1},\"input\":{\"type\":\"default\",\"@id\":1,\"activateable\":false,\"activationLimit\":100,\"capacity\":20,\"depreciate\":20,\"stored\":0},\"output\":{\"type\":\"default\",\"@id\":2,\"activateable\":false,\"activationLimit\":100,\"capacity\":20,\"depreciate\":20,\"stored\":0}}";

        try {
            TestSerializer ser = objectMapper.readValue(json, TestSerializer.class);

            // Currently connections can only be deserialized within the context of the Network class.
            // This is to handle circular references.  See NetworkDeserializer
            fail();
        } catch (Exception e) {
            // Exception expected
        }
    }


    @JsonTypeName("testSerializerConnection")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class TestSerializer {

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
