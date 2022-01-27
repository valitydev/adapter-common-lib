package dev.vality.adapter.common.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.adapter.common.mapper.SimpleObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;

public class AdapterContextTest {

    @Test
    public void testUnwrapped() throws IOException {
        AdapterContext adapterContext = new AdapterContext();
        adapterContext.setMd("kek");
        PollingInfo pollingInfo = new PollingInfo();
        pollingInfo.setMaxDateTimePolling(Instant.now());
        adapterContext.setPollingInfo(pollingInfo);
        ObjectMapper objectMapper = new SimpleObjectMapper().createSimpleObjectMapperFactory();
        String str = objectMapper.writeValueAsString(adapterContext);
        assertTrue(str.startsWith("{\"md\":\"kek\",\"max_date_time_polling\":"));
        AdapterContext adapterContextRestored = objectMapper.readValue(str, AdapterContext.class);
        assertEquals(adapterContext.getMd(), adapterContextRestored.getMd());
        assertNotNull(adapterContextRestored.getPollingInfo());
        assertEquals(adapterContext.getPollingInfo().getMaxDateTimePolling().truncatedTo(ChronoUnit.MILLIS),
                adapterContextRestored.getPollingInfo().getMaxDateTimePolling().truncatedTo(ChronoUnit.MILLIS)
        );
    }

}
