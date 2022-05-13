package dev.vality.adapter.common.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.adapter.common.mapper.ErrorMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

import java.io.IOException;

@RequiredArgsConstructor
public class SimpleErrorMapping {

    private final Resource filePath;
    private final String patternReason;

    public ErrorMapping createErrorMapping() throws IOException {
        var mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        return new ErrorMapping(filePath.getInputStream(), patternReason, mapper);
    }

}
