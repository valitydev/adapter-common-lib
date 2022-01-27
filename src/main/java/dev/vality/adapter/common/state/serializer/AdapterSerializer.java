package dev.vality.adapter.common.state.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.adapter.common.model.AdapterContext;

public class AdapterSerializer extends StateSerializer<AdapterContext> {

    public AdapterSerializer(ObjectMapper mapper) {
        super(mapper);
    }

}
