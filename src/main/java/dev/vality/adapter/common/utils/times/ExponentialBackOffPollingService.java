package dev.vality.adapter.common.utils.times;

import dev.vality.adapter.common.model.PollingInfo;
import dev.vality.adapter.common.state.backoff.BackOffExecution;
import dev.vality.adapter.common.state.backoff.ExponentialBackOff;
import dev.vality.adapter.common.state.backoff.TimeOptionsExtractors;

import java.time.Instant;
import java.util.Map;

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
        Integer exponential = TimeOptionsExtractors.extractExponent(options, ExponentialBackOff.DEFAULT_MUTIPLIER);
        Integer defaultInitialExponential = TimeOptionsExtractors.extractDefaultInitialExponential(options,
                ExponentialBackOff.DEFAULT_INITIAL_INTERVAL);
        Integer maxTimeBackOff =
                TimeOptionsExtractors.extractMaxTimeBackOff(options, ExponentialBackOff.DEFAULT_MAX_INTERVAL);

        return new ExponentialBackOff(
                startTime,
                currentLocalTime,
                exponential,
                defaultInitialExponential,
                maxTimeBackOff);
    }
}