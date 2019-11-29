package com.rbkmoney.adapter.common.state.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.common.model.Callback;

public class CallbackSerializer extends StateSerializer<Callback> {

    public CallbackSerializer(ObjectMapper mapper) {
        super(mapper);
    }

}
