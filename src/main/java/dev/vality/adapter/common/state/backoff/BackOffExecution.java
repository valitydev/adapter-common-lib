package dev.vality.adapter.common.state.backoff;

@FunctionalInterface
public interface BackOffExecution {
    Long nextBackOff();
}
