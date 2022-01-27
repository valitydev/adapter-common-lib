package dev.vality.adapter.common.state.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.adapter.common.model.Callback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class CallbackDeserializer implements Deserializer<Callback> {

    private final ObjectMapper mapper;

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

    @Override
    public Callback read(String data) {
        throw new DeserializationException("Deserialization not supported");
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
