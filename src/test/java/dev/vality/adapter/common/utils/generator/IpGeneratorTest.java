package dev.vality.adapter.common.utils.generator;

import org.junit.Assert;
import org.junit.Test;

public final class IpGeneratorTest {

    public static final String IP_V4 = "123.0.0.2";

    @Test
    public void checkAndGenerate() {
        String expectedIp = "127.0.0.1";
        String resultIp = IpGenerator.checkAndGenerate("2001:0db8:85a3:0000:0000:8a2e:0370:7334");
        Assert.assertEquals(expectedIp, resultIp);

        resultIp = IpGenerator.checkAndGenerate("");
        Assert.assertEquals(expectedIp, resultIp);

        resultIp = IpGenerator.checkAndGenerate(null);
        Assert.assertEquals(expectedIp, resultIp);

        resultIp = IpGenerator.checkAndGenerate(IP_V4);
        Assert.assertEquals(IP_V4, resultIp);
    }

}
