package com.rbkmoney.adapter.common.utils.mpi.utils;

import com.rbkmoney.adapter.common.utils.mpi.model.Message;
import com.rbkmoney.adapter.common.utils.mpi.model.PARes;
import com.rbkmoney.adapter.common.utils.mpi.model.TX;
import com.rbkmoney.adapter.common.utils.mpi.model.ThreeDSecure;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

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

        PARes paRes = new PARes();
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
