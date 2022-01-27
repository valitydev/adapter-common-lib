package dev.vality.adapter.common.state.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.adapter.common.model.AdapterContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
@AllArgsConstructor
public class AdapterDeserializer implements Deserializer<AdapterContext> {

    private final ObjectMapper mapper;

    public AdapterContext read(byte[] data) {
        if (data == null) {
            return new AdapterContext();
        }
        try {
            return getMapper().readValue(data, AdapterContext.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public AdapterContext read(String data) {
        throw new DeserializationException("Deserialization not supported");
    }

}
