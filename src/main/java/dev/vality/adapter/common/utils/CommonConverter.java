package dev.vality.adapter.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConverter {

    public static Map byteBufferToMap(ByteBuffer byteBuffer) throws IOException {
        return new ObjectMapper().readValue(new String(byteBuffer.array(), StandardCharsets.UTF_8), HashMap.class);
    }

    public static ByteBuffer mapToByteBuffer(Map<String, String> map) throws JacksonException {
        return ByteBuffer.wrap(new ObjectMapper().writeValueAsString(map).getBytes());
    }

    public static Map<String, String> mapArrayToMap(Map<String, String[]> map) {
        return Optional.ofNullable(map)
                .orElseGet(HashMap::new)
                .entrySet().stream()
                .collect(Collectors.toMap(k -> k.getKey().trim(),
                        v -> v.getValue()[0]));
    }

}
