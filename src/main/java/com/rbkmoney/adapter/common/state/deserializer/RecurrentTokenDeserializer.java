package com.rbkmoney.adapter.common.state.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.common.model.RecToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Base64;

@Getter
@Setter
@AllArgsConstructor
public class RecurrentTokenDeserializer implements Deserializer<RecToken> {

    private final ObjectMapper mapper;

    @Override
    public RecToken read(byte[] data) {
        throw new DeserializationException("Deserialization not supported");
    }

    @Override
    public RecToken read(String data) {
        if (data == null) {
            return new RecToken();
        }
        try {
            return getMapper().readValue(Base64.getDecoder().decode(data), RecToken.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
