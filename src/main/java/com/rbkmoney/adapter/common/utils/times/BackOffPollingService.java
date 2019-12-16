package com.rbkmoney.adapter.common.utils.times;

import com.rbkmoney.adapter.common.model.PollableContext;
import com.rbkmoney.adapter.common.state.backoff.BackOffExecution;

import java.util.Map;

public interface BackOffPollingService<T extends PollableContext> {

    public BackOffExecution prepareBackOffExecution(T pollableContext, Map<String, String> options);

    public int prepareNextPollingInterval(T pollableContext, Map<String, String> options);

}