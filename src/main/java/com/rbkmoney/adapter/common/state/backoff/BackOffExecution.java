package com.rbkmoney.adapter.common.state.backoff;

@FunctionalInterface
public interface BackOffExecution {
    Long nextBackOff();
}
