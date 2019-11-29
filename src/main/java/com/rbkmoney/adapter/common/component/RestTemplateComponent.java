package com.rbkmoney.adapter.common.component;

import com.rbkmoney.woody.api.trace.ContextUtils;
import com.rbkmoney.woody.api.trace.context.TraceContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.actuate.metrics.web.client.MetricsRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

public class RestTemplateComponent {

    public RestTemplate getSimpleRestTemplate(MetricsRestTemplateCustomizer metricsRestTemplateCustomizer,
                                              int networkTimeout) {
        HttpComponentsClientHttpRequestFactory requestFactory = getRequestFactory(getSimpleHttpClient());
        RestTemplateBuilder restTemplateBuilder = getRestTemplateBuilder(requestFactory, metricsRestTemplateCustomizer);
        return getRestTemplate(restTemplateBuilder, networkTimeout);
    }

    public RestTemplate getRestTemplateWithConverters(MetricsRestTemplateCustomizer metricsRestTemplateCustomizer,
                                                      List<HttpMessageConverter<?>> messageConverterList,
                                                      int networkTimeout) {
        HttpComponentsClientHttpRequestFactory requestFactory = getRequestFactory(getSimpleHttpClient());
        RestTemplateBuilder restTemplateBuilder = getRestTemplateBuilder(requestFactory, metricsRestTemplateCustomizer);
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

    public RestTemplateBuilder getRestTemplateBuilder(HttpComponentsClientHttpRequestFactory requestFactory,
                                                      MetricsRestTemplateCustomizer metricsRestTemplateCustomizer) {
        return new RestTemplateBuilder()
                .requestFactory(() -> requestFactory)
                .additionalCustomizers(metricsRestTemplateCustomizer);
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