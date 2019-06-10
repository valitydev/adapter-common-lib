package com.rbkmoney.adapter.common.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.common.model.Callback;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CallbackSerializer extends StateSerializer<Callback> {

    public CallbackSerializer(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Callback read(byte[] data) {
        if (data == null) {
            return new Callback();
        }
        try {
            return getMapper().readValue(data, Callback.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Callback read(HttpServletRequest request) {
        Map<String, String> stringMap = Optional.ofNullable(request.getParameterMap())
                .orElseGet(HashMap::new)
                .entrySet().stream()
                .collect(Collectors.toMap(k -> k.getKey().trim(),
                        v -> v.getValue()[0]));
        return mapper.convertValue(stringMap, Callback.class);
    }

}
