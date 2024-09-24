package org.neil.neural.serializer.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neil.neural.Node;
import org.neil.neural.NodeAlwaysEmpty;
import org.neil.neural.NodeAlwaysFull;
import org.neil.neural.NodeDefault;
import org.neil.neural.NodeDivisor;
import org.neil.neural.NodeMax;
import org.neil.neural.NodeMultiplier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NodeSerializerTest {

    ObjectMapper objectMapper;

    @BeforeEach
    public void before(){
        objectMapper = new ObjectMapper();
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY,
                false);
        objectMapper.configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature(),true);
    }

    @Test
    public void testDefaultNodeSerializer() throws Exception{
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new NodeDefault(1));

        System.out.println(json);
        Node node = objectMapper.readValue(json, NodeDefault.class);
    }

    @Test
    public void testNodeAlwaysEmpty() throws Exception{
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new NodeAlwaysEmpty(1));

        System.out.println(json);
        Node node = objectMapper.readValue(json, Node.class);
    }

    @Test
    public void testNodeAlwaysFull() throws Exception{
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new NodeAlwaysFull(1));

        System.out.println(json);
        Node node = objectMapper.readValue(json, Node.class);
    }

    @Test
    public void testNodeMax() throws Exception{
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new NodeMax(1));

        System.out.println(json);
        objectMapper.readValue(json, NodeMax.class);
        objectMapper.readValue(json, Node.class);
    }

    @Test
    public void testMultiplerNode() throws Exception {
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new NodeMultiplier(1));

        System.out.println(json);
        objectMapper.readValue(json, NodeMultiplier.class);
        objectMapper.readValue(json, Node.class);
    }

    @Test
    public void testDivisorNode() throws Exception {
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new NodeDivisor(1));

        System.out.println(json);
        objectMapper.readValue(json, NodeDivisor.class);
        objectMapper.readValue(json, Node.class);
    }

    @Test
    public void testNodes() throws JsonProcessingException {
        MultipleNodes multipleNodes = new MultipleNodes();
        multipleNodes.nodes = List.of(new NodeAlwaysEmpty(1),
                new NodeAlwaysFull(2),
                new NodeDefault(3));

        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(multipleNodes);

        System.out.println(json);
        MultipleNodes node = objectMapper.readValue(json, MultipleNodes.class);
        assertTrue(node.nodes.get(0) instanceof NodeAlwaysEmpty);
        assertTrue(node.nodes.get(1) instanceof NodeAlwaysFull);
        assertTrue(node.nodes.get(2) instanceof NodeDefault);
    }

    public static class MultipleNodes{
        @JsonProperty
        List<Node> nodes;
    }

}
