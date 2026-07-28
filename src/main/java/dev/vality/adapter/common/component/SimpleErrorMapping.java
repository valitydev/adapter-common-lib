package dev.vality.adapter.common.component;

import dev.vality.adapter.common.v2.mapper.ErrorMapping;
import org.springframework.core.io.Resource;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;

public record SimpleErrorMapping(Resource filePath, String patternReason) {

    public ErrorMapping createErrorMapping() throws IOException {
        var mapper = JsonMapper.builder()
                .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
                .build();
        return new ErrorMapping(filePath.getInputStream(), patternReason, mapper);
    }

}
