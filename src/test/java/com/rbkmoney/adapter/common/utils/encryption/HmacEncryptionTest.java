package com.rbkmoney.adapter.common.utils.encryption;

import org.junit.Test;

import static com.rbkmoney.adapter.common.utils.encryption.HmacEncryption.calculateHMacSha256;
import static org.junit.Assert.assertEquals;

public class HmacEncryptionTest {

    private String message = "511.483USD677144616IT Books. Qty: 217Books Online Inc." +
            "14www.sample.com1512345678901234589999999919pgw@mail.sample.com11--" +
            "142003010515302116F2B2DD7E603A7ADA33https://www.sample.com/shop/reply";
    private String secret = "00112233445566778899AABBCCDDEEFF";

    @Test
    public void testHMacSha256() {
        String expectedHMac = "3fd9ee801d3aeda5d33af83580279ef920ad78e8883a0b9bc3942c74129db2f0";
        String hmac = calculateHMacSha256(message, secret);
        assertEquals("HMAC SHA256 is not equal expected", expectedHMac, hmac);
    }

}
