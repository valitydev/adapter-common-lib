package com.rbkmoney.adapter.common.utils.times;

import com.rbkmoney.adapter.common.model.PollingInfo;
import com.rbkmoney.adapter.common.state.backoff.BackOffExecution;

import java.util.Map;

public interface BackOffPollingService<T extends PollingInfo> {

    public BackOffExecution prepareBackOffExecution(T pollableContext, Map<String, String> options);

    public int prepareNextPollingInterval(T pollableContext, Map<String, String> options);

}