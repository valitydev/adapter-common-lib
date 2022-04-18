package dev.vality.adapter.common.utils.times;

import dev.vality.adapter.common.model.PollingInfo;
import dev.vality.adapter.common.state.backoff.BackOffExecution;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExponentialBackOffPollingServiceTest {

    ExponentialBackOffPollingService exponentialBackOffPollingService =
            new ExponentialBackOffPollingService<PollingInfo>();

    @Test
    public void testPrepareBackOffExecution() throws InterruptedException {
        PollingInfo pollingInfo = new PollingInfo();
        BackOffExecution backOffExecution =
                exponentialBackOffPollingService.prepareBackOffExecution(pollingInfo, new HashMap<>());
        Long result = backOffExecution.nextBackOff();
        assertEquals(2, result.longValue());
        pollingInfo.setMaxDateTimePolling(Instant.now());
        backOffExecution = exponentialBackOffPollingService.prepareBackOffExecution(pollingInfo, new HashMap<>());
        result = backOffExecution.nextBackOff();
        assertEquals(2, result.longValue());
    }

    @Test
    public void testPrepareNextPollingIntervalWithDefaultValues() throws InterruptedException {
        PollingInfo pollingInfo = new PollingInfo();
        Instant now = Instant.now();
        pollingInfo.setStartDateTimePolling(now);
        BackOffExecution backOffExecution =
                exponentialBackOffPollingService.prepareBackOffExecution(pollingInfo, new HashMap<>());
        Long result = backOffExecution.nextBackOff();
        assertEquals(2, result.longValue());

        Thread.sleep(result * 1000L);
        int nextInterval = exponentialBackOffPollingService.prepareNextPollingInterval(pollingInfo, new HashMap<>());
        assertEquals(4, nextInterval);

        Thread.sleep(nextInterval * 1000L);
        nextInterval = exponentialBackOffPollingService.prepareNextPollingInterval(pollingInfo, new HashMap<>());
        assertEquals(8, nextInterval);
    }

    @Test
    public void testPrepareNextPollingIntervalWithInitialEq1() throws InterruptedException {
        PollingInfo pollingInfo = new PollingInfo();
        Instant now = Instant.now();
        pollingInfo.setStartDateTimePolling(now);
        HashMap<String, String> options = new HashMap<>();
        options.put("default_initial_exponential", "1");
        BackOffExecution backOffExecution =
                exponentialBackOffPollingService.prepareBackOffExecution(pollingInfo, options);
        Long result = backOffExecution.nextBackOff();
        assertEquals(1, result.longValue());

        Thread.sleep(result * 1000L);
        int nextInterval = exponentialBackOffPollingService.prepareNextPollingInterval(pollingInfo, options);
        assertEquals(2, nextInterval);

        Thread.sleep(nextInterval * 1000L);
        nextInterval = exponentialBackOffPollingService.prepareNextPollingInterval(pollingInfo, options);
        assertEquals(4, nextInterval);
    }

    @Test
    public void testPrepareNextPollingIntervalWithExponentialEq1() throws InterruptedException {
        PollingInfo pollingInfo = new PollingInfo();
        Instant now = Instant.now();
        pollingInfo.setStartDateTimePolling(now);
        HashMap<String, String> options = new HashMap<>();
        options.put("exponential", "1");
        BackOffExecution backOffExecution =
                exponentialBackOffPollingService.prepareBackOffExecution(pollingInfo, options);
        Long result = backOffExecution.nextBackOff();
        assertEquals(2, result.longValue());

        Thread.sleep(result * 1000L);
        int nextInterval = exponentialBackOffPollingService.prepareNextPollingInterval(pollingInfo, options);
        assertEquals(2, nextInterval);

        Thread.sleep(nextInterval * 1000L);
        nextInterval = exponentialBackOffPollingService.prepareNextPollingInterval(pollingInfo, options);
        assertEquals(2, nextInterval);
    }
}