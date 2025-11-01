package dev.vality.adapter.common.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.adapter.common.v2.mapper.ErrorMapping;
import org.springframework.core.io.Resource;

import java.io.IOException;

public record SimpleErrorMapping(Resource filePath, String patternReason) {

    public ErrorMapping createErrorMapping() throws IOException {
        var mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        return new ErrorMapping(filePath.getInputStream(), patternReason, mapper);
    }

}
