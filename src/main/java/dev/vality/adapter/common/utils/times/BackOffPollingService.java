package dev.vality.adapter.common.utils.times;

import dev.vality.adapter.common.model.PollingInfo;
import dev.vality.adapter.common.state.backoff.BackOffExecution;

import java.util.Map;

public interface BackOffPollingService<T extends PollingInfo> {

    public BackOffExecution prepareBackOffExecution(T pollableContext, Map<String, String> options);

    public int prepareNextPollingInterval(T pollableContext, Map<String, String> options);

}