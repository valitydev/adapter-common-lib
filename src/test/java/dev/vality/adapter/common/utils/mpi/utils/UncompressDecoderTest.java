package dev.vality.adapter.common.utils.mpi.utils;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class UncompressDecoderTest {

    @Test
    public void decodeAndExtractThreeDSecure() {
        String string = UncompressDecoder.decodePaRes(TestDataThreeDSecure.PARES);
        assertNotNull(string);
        assertEquals(TestDataThreeDSecure.XML, string);
    }

}
