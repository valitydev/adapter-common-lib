package com.rbkmoney.adapter.common.properties;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public abstract class TimerProperties {

    @NotNull
    private int redirectTimeout;

    @NotNull
    private int maxTimePolling;

    @NotNull
    private int pollingDelay;

}
