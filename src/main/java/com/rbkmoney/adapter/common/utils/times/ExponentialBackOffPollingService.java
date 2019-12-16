package com.rbkmoney.adapter.common.utils.times;

import com.rbkmoney.adapter.common.model.PollableContext;
import com.rbkmoney.adapter.common.state.backoff.BackOffExecution;
import com.rbkmoney.adapter.common.state.backoff.ExponentialBackOff;
import com.rbkmoney.adapter.common.state.backoff.TimeOptionsExtractors;

import java.time.Instant;
import java.util.Map;

import static com.rbkmoney.adapter.common.state.backoff.ExponentialBackOff.*;

public class ExponentialBackOffPollingService<T extends PollableContext> {

    public BackOffExecution prepareBackOffExecution(T adapterContext, Map<String, String> options) {
        return exponentialBackOff(adapterContext, options)
                .start();
    }

    public int prepareNextPollingInterval(T adapterContext, Map<String, String> options) {
        return exponentialBackOff(adapterContext, options)
                .start()
                .nextBackOff()
                .intValue();
    }

    private ExponentialBackOff exponentialBackOff(T adapterContext, Map<String, String> options) {
        final Long currentLocalTime = Instant.now().toEpochMilli();

        Long startTime = adapterContext.getStartDateTimePolling() != null
                ? adapterContext.getStartDateTimePolling().toEpochMilli()
                : currentLocalTime;
        Integer exponential = TimeOptionsExtractors.extractExponent(options, DEFAULT_EXPONENTIAL);
        Integer defaultInitialExponential = TimeOptionsExtractors.extractDefaultInitialExponential(options, DEFAULT_INITIAL_EXPONENTIAL);
        Integer maxTimeBackOff = TimeOptionsExtractors.extractMaxTimeBackOff(options, DEFAULT_MAX_TIME_BACK_OFF);

        return new ExponentialBackOff(
                startTime,
                currentLocalTime,
                exponential,
                defaultInitialExponential,
                maxTimeBackOff);
    }
}