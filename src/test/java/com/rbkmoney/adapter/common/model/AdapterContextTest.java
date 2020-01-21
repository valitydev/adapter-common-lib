package com.rbkmoney.adapter.common.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.common.mapper.SimpleObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;

import static org.junit.Assert.*;

public class AdapterContextTest {

    @Test
    public void testUnwrapped() throws IOException {
        ObjectMapper om = new SimpleObjectMapper().createSimpleObjectMapperFactory();
        AdapterContext ac = new AdapterContext();
        ac.setMd("kek");
        PollingInfo pollingInfo = new PollingInfo();
        pollingInfo.setMaxDateTimePolling(Instant.now());
        ac.setPollingInfo(pollingInfo);
        String str = om.writeValueAsString(ac);
        assertTrue(str.startsWith("{\"md\":\"kek\",\"max_date_time_polling\":"));
        AdapterContext acRestored = om.readValue(str, AdapterContext.class);
        assertEquals(ac.getMd(), acRestored.getMd());
        assertNotNull(acRestored.getPollingInfo());
        assertEquals(ac.getPollingInfo().getMaxDateTimePolling(), acRestored.getPollingInfo().getMaxDateTimePolling());
    }

}
