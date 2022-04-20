package dev.vality.adapter.common.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.vality.adapter.common.mapper.naming.strategy.UpperSnakeCaseStrategy;

public class SimpleObjectMapper {

    public ObjectMapper createSimpleObjectMapperFactory() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule())
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public ObjectMapper createUpperSnakeCaseStrategyMapper() {
        return createSimpleObjectMapperFactory()
                .setPropertyNamingStrategy(new UpperSnakeCaseStrategy());
    }
}
