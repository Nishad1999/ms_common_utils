package org.mystore.config;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class NullSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object o, com.fasterxml.jackson.core.JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        try {
            jsonGenerator.writeString("");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}