package org.neil.neural.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.neil.neural.Node;
import org.neil.neural.NodeAlwaysEmpty;
import org.neil.neural.NodeAlwaysFull;
import org.neil.neural.NodeDefault;
import org.neil.neural.NodeDivisor;
import org.neil.neural.NodeMax;
import org.neil.neural.NodeMultiplier;

import java.io.IOException;
import java.util.Map;

public class NodeDeserializer extends StdDeserializer<Node> {

    public static final Map<String,Class<? extends Node>> NODE_IMPLEMENTER;

    static {
        NODE_IMPLEMENTER = Map.of("default",NodeDefault.class,
                "alwaysFull",NodeAlwaysFull.class,
                "alwaysEmpty",NodeAlwaysEmpty.class,
                "nodeMax", NodeMax.class,
                "nodeDivisor", NodeDivisor.class,
                "nodeMultiplier", NodeMultiplier.class);
    }

    public NodeDeserializer() {
        super(Node.class);
    }

    @Override
    public Node deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String nodeType = node.get("type").textValue();

        return jsonParser.readValueAs(NODE_IMPLEMENTER.get(nodeType));
    }
}
