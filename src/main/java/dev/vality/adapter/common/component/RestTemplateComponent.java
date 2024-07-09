package dev.vality.adapter.common.component;

import dev.vality.woody.api.trace.ContextUtils;
import dev.vality.woody.api.trace.context.TraceContext;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.boot.actuate.metrics.web.client.ObservationRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

public class RestTemplateComponent {

    public RestTemplate getSimpleRestTemplate(ObservationRestTemplateCustomizer observationRestTemplateCustomizer,
                                              int networkTimeout) {
        HttpComponentsClientHttpRequestFactory requestFactory = getRequestFactory(getSimpleHttpClient());
        RestTemplateBuilder restTemplateBuilder =
                getRestTemplateBuilder(requestFactory, observationRestTemplateCustomizer);
        return getRestTemplate(restTemplateBuilder, networkTimeout);
    }

    public RestTemplate getRestTemplateWithConverters(
            ObservationRestTemplateCustomizer observationRestTemplateCustomizer,
            List<HttpMessageConverter<?>> messageConverterList,
            int networkTimeout
    ) {
        HttpComponentsClientHttpRequestFactory requestFactory = getRequestFactory(getSimpleHttpClient());
        RestTemplateBuilder restTemplateBuilder =
                getRestTemplateBuilder(requestFactory, observationRestTemplateCustomizer);
        RestTemplate restTemplate = getRestTemplate(restTemplateBuilder, networkTimeout);
        restTemplate.setMessageConverters(messageConverterList);
        return restTemplate;
    }

    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder, int networkTimeout) {
        int executionTimeout =
                ContextUtils.getExecutionTimeout(TraceContext.getCurrentTraceData().getServiceSpan(), networkTimeout);
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(executionTimeout))
                .setReadTimeout(Duration.ofMillis(executionTimeout))
                .build();
    }

    public RestTemplateBuilder getRestTemplateBuilder(
            HttpComponentsClientHttpRequestFactory requestFactory,
            ObservationRestTemplateCustomizer observationRestTemplateCustomizer
    ) {
        return new RestTemplateBuilder()
                .requestFactory(() -> requestFactory)
                .additionalCustomizers(observationRestTemplateCustomizer);
    }

    public HttpComponentsClientHttpRequestFactory getRequestFactory(CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return requestFactory;
    }

    public CloseableHttpClient getSimpleHttpClient() {
        return HttpClients.custom()
                .disableAutomaticRetries()
                .build();
    }
}