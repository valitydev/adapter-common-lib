package com.rbkmoney.adapter.common.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.error.mapping.ErrorMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

import java.io.IOException;

@RequiredArgsConstructor
public class SimpleErrorMapping {

    private final Resource filePath;
    private final String patternReason;

    public ErrorMapping getErrorMapping() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        ErrorMapping errorMapping = new ErrorMapping(filePath.getInputStream(), patternReason, mapper);
        errorMapping.validateMapping();
        return errorMapping;
    }

}
