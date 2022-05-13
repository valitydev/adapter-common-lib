package dev.vality.adapter.common.damsel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

import static java.lang.Integer.parseInt;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionsExtractors {

    public static final String TIMER_REDIRECT_TIMEOUT = "redirect_timeout";
    public static final String TIMER_MAX_TIME_POLLING = "max_time_polling";
    public static final String TIMER_POLLING_DELAY = "polling_delay";

    public static Integer extractMaxTimePolling(Map<String, String> options, int maxTimePolling) {
        return parseInt(options.getOrDefault(TIMER_MAX_TIME_POLLING, String.valueOf(maxTimePolling)));
    }

    public static Integer extractRedirectTimeout(Map<String, String> options, int redirectTimeout) {
        return parseInt(options.getOrDefault(TIMER_REDIRECT_TIMEOUT, String.valueOf(redirectTimeout)));
    }

    public static Integer extractPollingDelay(Map<String, String> options, int pollingDelay) {
        return parseInt(options.getOrDefault(TIMER_POLLING_DELAY, String.valueOf(pollingDelay)));
    }

}
