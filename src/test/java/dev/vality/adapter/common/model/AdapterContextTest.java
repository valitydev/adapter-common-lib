package dev.vality.adapter.common.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.adapter.common.mapper.SimpleObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;

import static org.junit.Assert.*;

public class AdapterContextTest {

    @Test
    public void testUnwrapped() throws IOException {
        AdapterContext ac = new AdapterContext();
        ac.setMd("kek");
        PollingInfo pollingInfo = new PollingInfo();
        pollingInfo.setMaxDateTimePolling(Instant.now());
        ac.setPollingInfo(pollingInfo);
        ObjectMapper objectMapper = new SimpleObjectMapper().createSimpleObjectMapperFactory();
        String str = objectMapper.writeValueAsString(ac);
        assertTrue(str.startsWith("{\"md\":\"kek\",\"max_date_time_polling\":"));
        AdapterContext acRestored = objectMapper.readValue(str, AdapterContext.class);
        assertEquals(ac.getMd(), acRestored.getMd());
        assertNotNull(acRestored.getPollingInfo());
        assertEquals(ac.getPollingInfo().getMaxDateTimePolling(), acRestored.getPollingInfo().getMaxDateTimePolling());
    }

}
