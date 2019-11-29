package com.rbkmoney.adapter.common.state.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.common.model.AdapterContext;

public class AdapterSerializer extends StateSerializer<AdapterContext> {

    public AdapterSerializer(ObjectMapper mapper) {
        super(mapper);
    }

}
