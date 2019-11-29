package com.rbkmoney.adapter.common.state.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.common.model.RecToken;

public class RecurrentTokenSerializer extends StateSerializer<RecToken> {

    public RecurrentTokenSerializer(ObjectMapper mapper) {
        super(mapper);
    }

}
