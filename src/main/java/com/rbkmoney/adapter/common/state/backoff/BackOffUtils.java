package com.rbkmoney.adapter.common.state.backoff;

import com.rbkmoney.adapter.common.model.AdapterContext;

import java.util.Map;

public class BackOffUtils {
    public static BackOffExecution prepareBackOffExecution(AdapterContext adapterContext, Map<String, String> options) {
        ExponentialBackOff exponentialBackOff = new ExponentialBackOff(adapterContext, options);
        return exponentialBackOff.start();
    }

    public static int prepareNextPollingInterval(AdapterContext adapterContext, Map<String, String> options) {
        ExponentialBackOff exponentialBackOff = new ExponentialBackOff(adapterContext, options);
        return exponentialBackOff.start().nextBackOff().intValue();
    }
}
