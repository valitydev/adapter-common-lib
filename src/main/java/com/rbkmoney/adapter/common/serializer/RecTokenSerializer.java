package com.rbkmoney.adapter.common.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.common.model.RecToken;

import java.io.IOException;
import java.util.Base64;

public class RecTokenSerializer extends StateSerializer<RecToken> {

    public RecTokenSerializer(ObjectMapper mapper) {
        super(mapper);
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
