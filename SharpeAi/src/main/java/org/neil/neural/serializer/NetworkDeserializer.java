package org.neil.neural.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.neil.neural.Connection;
import org.neil.neural.Network;
import org.neil.neural.Node;
import org.neil.neural.input.InputNode;
import org.neil.neural.output.OutputNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NetworkDeserializer extends StdDeserializer<Network> {

    public NetworkDeserializer() {
        super(Network.class);
    }

    @Override
    public Network deserialize(JsonParser jsonParser,
                               DeserializationContext deserializationContext) throws IOException, JacksonException {

        JsonNode root = jsonParser.getCodec().readTree(jsonParser);

        List<InputNode> inputNodes = Arrays.asList(jsonParser.getCodec().treeToValue(root.get("inputs"), InputNode[].class));
        List<OutputNode> outputNode = Arrays.asList(jsonParser.getCodec().treeToValue(root.get("outputs"), OutputNode[].class));
        List<Node> intermediate = Arrays.asList(jsonParser.getCodec().treeToValue(root.get("intermediate"), Node[].class));

        Map<Integer,Node> nodeCache = Stream.of(inputNodes,outputNode,intermediate)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Node::getId, x->x));

        List<Connection> connections = new ArrayList<>();

        for (final JsonNode item : root.get("connections")) {
            Node source = nodeCache.get(item.get("source").intValue());
            Node destination = nodeCache.get(item.get("destination").intValue());
            int bandwidth = item.get("bandwidth").intValue();
            double multiplier = item.get("multiplier").doubleValue();
            Connection.ConnectionType connectionType = Connection.ConnectionType.valueOf(item.get("connectionType").asText());

            connections.add(new Connection(source,
                    destination,
                    bandwidth,
                    multiplier,
                    connectionType));
        }


        return new Network(inputNodes,
                outputNode,
                intermediate,
                connections);
    }

    private <T> List<T> getNodes(String name,
                                 JsonNode node,
                                 DeserializationContext deserializationContext,
                                 Class<T> type) throws IOException {
        JsonNode reader = node.get(name);


        List<T> toReturn = new ArrayList<>();
        if (reader.isArray()) {
            for (final JsonNode item : reader) {
                toReturn.add(deserializationContext.readValue(item.traverse(), type));
            }
        } else {
            throw new IllegalStateException("Array Expected");
        }
        return toReturn;
    }
}
