package com.rbkmoney.adapter.common.state.backoff;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExponentialBackOff implements BackOff {

    public static final int DEFAULT_MILLIS = 1000;

    public static final Integer DEFAULT_EXPONENTIAL = 2;
    public static final Integer DEFAULT_INITIAL_EXPONENTIAL = 2;
    public static final Integer DEFAULT_MAX_TIME_BACK_OFF = 300;

    private int millis = DEFAULT_MILLIS;
    private Integer exponential = DEFAULT_EXPONENTIAL;
    private Integer defaultInitialExponential = DEFAULT_INITIAL_EXPONENTIAL;
    private Integer maxTimeBackOff = DEFAULT_MAX_TIME_BACK_OFF;

    private Long startTime;
    private Long currentTime;

    public ExponentialBackOff(
            Long startTime,
            Long currentTime,
            Integer exponential,
            Integer defaultInitialExponential,
            Integer maxTimeBackOff) {
        this.startTime = startTime;
        this.currentTime = currentTime;
        this.exponential = exponential;
        this.defaultInitialExponential = defaultInitialExponential;
        this.maxTimeBackOff = maxTimeBackOff;
    }

    @Override
    public BackOffExecution start() {
        return new ExponentialBackOffExecution();
    }

    private class ExponentialBackOffExecution implements BackOffExecution {
        @Override
        public Long nextBackOff() {
            if (ExponentialBackOff.this.currentTime.equals(ExponentialBackOff.this.startTime)) {
                return Long.valueOf(ExponentialBackOff.this.defaultInitialExponential);
            }

            long nextBackOff = computeNextInterval(
                    ExponentialBackOff.this.exponential,
                    ExponentialBackOff.this.startTime,
                    ExponentialBackOff.this.currentTime);

            if (nextBackOff > ExponentialBackOff.this.maxTimeBackOff) {
                nextBackOff = (long) ExponentialBackOff.this.maxTimeBackOff;
            }

            return nextBackOff;
        }

        private long computeNextInterval(int exponent, Long startTime, Long currentTime) {
            return ((currentTime - startTime) / exponent / ExponentialBackOff.this.millis) * exponent + exponent;
        }
    }

}
