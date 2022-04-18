package dev.vality.adapter.common.utils.mpi.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UncompressDecoderTest {

    @Test
    public void decodeAndExtractThreeDSecure() {
        String string = UncompressDecoder.decodePaRes(TestDataThreeDSecure.PARES);
        assertNotNull(string);
        assertEquals(TestDataThreeDSecure.XML, string);
    }

}
