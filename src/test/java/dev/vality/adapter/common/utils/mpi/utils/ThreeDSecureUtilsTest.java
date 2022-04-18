package dev.vality.adapter.common.utils.mpi.utils;

import dev.vality.adapter.common.utils.mpi.model.Message;
import dev.vality.adapter.common.utils.mpi.model.PaRes;
import dev.vality.adapter.common.utils.mpi.model.TX;
import dev.vality.adapter.common.utils.mpi.model.ThreeDSecure;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ThreeDSecureUtilsTest {

    @Test
    public void extractThreeDSecureUtils() {
        ThreeDSecure threeDSecure = ThreeDSecureUtils.extractThreeDSecure(TestDataThreeDSecure.XML);
        assertNotNull(threeDSecure);
        assertNotNull(threeDSecure.getMessage().getPaRes().getTx().getEci());
        assertEquals(TestDataThreeDSecure.ECI_05, threeDSecure.getMessage().getPaRes().getTx().getEci());
    }

    @Test
    public void extractEciFromPaResTest() {
        TX tx = new TX();
        tx.setEci(TestDataThreeDSecure.ECI_05);

        PaRes paRes = new PaRes();
        paRes.setTx(tx);

        Message message = new Message();
        message.setPaRes(paRes);

        ThreeDSecure threeDSecure = new ThreeDSecure();
        threeDSecure.setMessage(message);

        String eci = ThreeDSecureUtils.extractEciFromPaRes(threeDSecure);
        assertEquals(TestDataThreeDSecure.ECI_05, eci);

        threeDSecure.getMessage().getPaRes().getTx().setEci(TestDataThreeDSecure.ECI_07);
        eci = ThreeDSecureUtils.extractEciFromPaRes(threeDSecure);
        assertEquals(TestDataThreeDSecure.ECI_07, eci);

        threeDSecure.getMessage().getPaRes().getTx().setEci(TestDataThreeDSecure.ECI_EMPTY);
        eci = ThreeDSecureUtils.extractEciFromPaRes(threeDSecure);
        assertEquals(TestDataThreeDSecure.ECI_EMPTY, eci);
    }

}
