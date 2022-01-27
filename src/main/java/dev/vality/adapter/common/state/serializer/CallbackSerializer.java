package dev.vality.adapter.common.state.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.adapter.common.model.Callback;

public class CallbackSerializer extends StateSerializer<Callback> {

    public CallbackSerializer(ObjectMapper mapper) {
        super(mapper);
    }

}
