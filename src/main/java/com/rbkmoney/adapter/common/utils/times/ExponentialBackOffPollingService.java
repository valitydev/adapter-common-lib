package com.rbkmoney.adapter.common.utils.times;

import com.rbkmoney.adapter.common.model.PollingInfo;
import com.rbkmoney.adapter.common.state.backoff.BackOffExecution;
import com.rbkmoney.adapter.common.state.backoff.ExponentialBackOff;
import com.rbkmoney.adapter.common.state.backoff.TimeOptionsExtractors;

import java.time.Instant;
import java.util.Map;

import static com.rbkmoney.adapter.common.state.backoff.ExponentialBackOff.*;

public class ExponentialBackOffPollingService<T extends PollingInfo> {

    public BackOffExecution prepareBackOffExecution(T pollingInfo, Map<String, String> options) {
        return exponentialBackOff(pollingInfo, options)
                .start();
    }

    public int prepareNextPollingInterval(T pollingInfo, Map<String, String> options) {
        return exponentialBackOff(pollingInfo, options)
                .start()
                .nextBackOff()
                .intValue();
    }

    private ExponentialBackOff exponentialBackOff(T pollingInfo, Map<String, String> options) {
        final Long currentLocalTime = Instant.now().toEpochMilli();

        Long startTime = pollingInfo.getStartDateTimePolling() != null
                ? pollingInfo.getStartDateTimePolling().toEpochMilli()
                : currentLocalTime;
        Integer exponential = TimeOptionsExtractors.extractExponent(options, DEFAULT_MUTIPLIER);
        Integer defaultInitialExponential = TimeOptionsExtractors.extractDefaultInitialExponential(options, DEFAULT_INITIAL_INTERVAL);
        Integer maxTimeBackOff = TimeOptionsExtractors.extractMaxTimeBackOff(options, DEFAULT_MAX_INTERVAL);

        return new ExponentialBackOff(
                startTime,
                currentLocalTime,
                exponential,
                defaultInitialExponential,
                maxTimeBackOff);
    }
}