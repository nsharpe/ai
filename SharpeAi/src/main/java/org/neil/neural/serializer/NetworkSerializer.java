package org.neil.neural.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.neil.neural.Connection;
import org.neil.neural.Network;
import org.neil.neural.Node;
import com.fasterxml.jackson.databind.Module;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class NetworkSerializer extends StdSerializer<Network> {

    protected NetworkSerializer() {
        super(Network.class);
    }

    @Override
    public void serialize(Network network, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        List<Connection> connections =  network.streamConnections().toList();

        jsonGenerator.writeStartObject();

        write("inputs",network.getInputs(),jsonGenerator);
        write("outputs",network.getOutputs(),jsonGenerator);
        write("intermediate",network.getIntermediateNodes(),jsonGenerator);
        write("connections",connections,jsonGenerator);


        jsonGenerator.writeEndObject();
    }

    private static <T> void write(String fieldName, Collection<T> items, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeFieldName(fieldName);
        jsonGenerator.writeStartArray();
        for(T item: items){
            jsonGenerator.writeObject(item);
        }
        jsonGenerator.writeEndArray();
    }
}
