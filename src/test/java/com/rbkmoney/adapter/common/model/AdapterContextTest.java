package com.rbkmoney.adapter.common.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;

public class AdapterContextTest {

    @Test
    public void testUnwrapped() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        AdapterContext ac = new AdapterContext();
        ac.setMd("kek");
        PollingInfo pollingInfo = new PollingInfo();
        pollingInfo.setMaxDateTimePolling(Instant.now());
        ac.setPollingInfo(pollingInfo);
        String str = om.writeValueAsString(ac);
        assertTrue(str.startsWith("{\"md\":\"kek\",\"max_date_time_polling\":"));
    }

}
