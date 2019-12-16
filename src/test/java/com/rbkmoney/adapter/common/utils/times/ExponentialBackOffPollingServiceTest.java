package com.rbkmoney.adapter.common.utils.times;

import com.rbkmoney.adapter.common.model.AdapterContext;
import com.rbkmoney.adapter.common.state.backoff.BackOffExecution;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.HashMap;

public class ExponentialBackOffPollingServiceTest {

    ExponentialBackOffPollingService exponentialBackOffPollingService = new ExponentialBackOffPollingService<AdapterContext>();

    @Test
    public void prepareBackOffExecution() throws InterruptedException {
        AdapterContext adapterContext = new AdapterContext();
        BackOffExecution backOffExecution = exponentialBackOffPollingService.prepareBackOffExecution(adapterContext, new HashMap<>());
        Long result = backOffExecution.nextBackOff();
        Assert.assertEquals(2, result.longValue());
        adapterContext.setStartDateTimePolling(Instant.now());
        backOffExecution = exponentialBackOffPollingService.prepareBackOffExecution(adapterContext, new HashMap<>());
        result = backOffExecution.nextBackOff();
        Assert.assertEquals(2, result.longValue());
    }

    @Test
    public void prepareNextPollingInterval() throws InterruptedException {
        AdapterContext adapterContext = new AdapterContext();
        adapterContext.setStartDateTimePolling(Instant.now());
        BackOffExecution backOffExecution = exponentialBackOffPollingService.prepareBackOffExecution(adapterContext, new HashMap<>());
        backOffExecution = backOffExecution;
        Long result = backOffExecution.nextBackOff();
        Assert.assertEquals(2, result.longValue());

        Thread.sleep(2000L);
        int nextInterval = exponentialBackOffPollingService.prepareNextPollingInterval(adapterContext, new HashMap<>());
        Assert.assertEquals(4, nextInterval);
    }
}