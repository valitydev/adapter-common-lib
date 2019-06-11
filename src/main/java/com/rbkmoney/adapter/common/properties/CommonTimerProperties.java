package com.rbkmoney.adapter.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Validated
public class CommonTimerProperties {

    @NotNull
    private int redirectTimeout;

    @NotNull
    private int maxTimePolling;

    @NotNull
    private int pollingDelay;

}
