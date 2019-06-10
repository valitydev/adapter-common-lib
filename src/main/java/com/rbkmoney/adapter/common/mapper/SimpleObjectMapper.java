package com.rbkmoney.adapter.common.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.common.mapper.naming.strategy.UpperSnakeCaseStrategy;

public class SimpleObjectMapper {

    public ObjectMapper getSimpleObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public ObjectMapper getUpperSnakeCaseStrategyMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(new UpperSnakeCaseStrategy())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}
