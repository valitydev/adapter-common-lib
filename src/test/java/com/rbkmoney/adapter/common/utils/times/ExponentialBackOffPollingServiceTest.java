package com.rbkmoney.adapter.common.utils.times;

import com.rbkmoney.adapter.common.model.AdapterContext;
import com.rbkmoney.adapter.common.model.PollingInfo;
import com.rbkmoney.adapter.common.state.backoff.BackOffExecution;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.HashMap;

public class ExponentialBackOffPollingServiceTest {

    ExponentialBackOffPollingService exponentialBackOffPollingService = new ExponentialBackOffPollingService<PollingInfo>();

    @Test
    public void prepareBackOffExecution() throws InterruptedException {
        PollingInfo pollingInfo = new PollingInfo();
        BackOffExecution backOffExecution = exponentialBackOffPollingService.prepareBackOffExecution(pollingInfo, new HashMap<>());
        Long result = backOffExecution.nextBackOff();
        Assert.assertEquals(2, result.longValue());
        pollingInfo.setMaxDateTimePolling(Instant.now());
        backOffExecution = exponentialBackOffPollingService.prepareBackOffExecution(pollingInfo, new HashMap<>());
        result = backOffExecution.nextBackOff();
        Assert.assertEquals(2, result.longValue());
    }

    @Test
    public void prepareNextPollingInterval() throws InterruptedException {
        PollingInfo pollingInfo = new PollingInfo();
        Instant now = Instant.now();
        pollingInfo.setStartDateTimePolling(now);
        BackOffExecution backOffExecution = exponentialBackOffPollingService.prepareBackOffExecution(pollingInfo, new HashMap<>());
        backOffExecution = backOffExecution;
        Long result = backOffExecution.nextBackOff();
        Assert.assertEquals(2, result.longValue());

        Thread.sleep(2000L);
        pollingInfo.setStartDateTimePolling(now);
        int nextInterval = exponentialBackOffPollingService.prepareNextPollingInterval(pollingInfo, new HashMap<>());
        Assert.assertEquals(4, nextInterval);

        Thread.sleep(nextInterval * 1000L);
        pollingInfo.setStartDateTimePolling(now);
        nextInterval = exponentialBackOffPollingService.prepareNextPollingInterval(pollingInfo, new HashMap<>());
        Assert.assertEquals(8, nextInterval);
    }
}