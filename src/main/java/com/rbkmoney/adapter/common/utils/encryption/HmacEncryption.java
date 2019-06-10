package com.rbkmoney.adapter.common.utils.encryption;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.util.MultiValueMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HmacEncryption {

    public static final String HMAC_SHA1 = "HmacSHA1";

    public static final String HMAC_SHA256 = "HmacSHA256";

    public static String calculateHMacSha1(String data, String key) {
        return calculateHMAC(data, key, HMAC_SHA1);
    }

    public static String calculateHMacSha256(String data, String key) {
        return calculateHMAC(data, key, HMAC_SHA256);
    }

    public static String calculateHMAC(String data, String key, String algorithm) {

        try {
            byte[] decodedKey = Hex.decodeHex(key.toCharArray());
            SecretKeySpec keySpec = new SecretKeySpec(decodedKey, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(keySpec);

            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] signatureBytes = mac.doFinal(dataBytes);

            return new String(new Hex().encode(signatureBytes));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String prepareDataForHmac(String[] fields, MultiValueMap<String, String> params) {
        StringBuilder dataHmac = new StringBuilder();
        Arrays.asList(fields)
                .forEach(field -> {
                            if (params.get(field) != null
                                    && !params.get(field).isEmpty()
                                    && params.get(field).get(0) != null
                                    && !params.get(field).get(0).isEmpty()
                                    ) {
                                dataHmac.append(params.get(field).get(0).length());
                                dataHmac.append(params.get(field).get(0));
                            } else {
                                dataHmac.append("-");
                            }
                        }
                );

        return dataHmac.toString();
    }

    public static String sign(String[] fieldsForSign,
                              MultiValueMap<String, String> params,
                              String key,
                              String algorithm) {
        String dataHmac = prepareDataForHmac(fieldsForSign, params);
        String pSign = calculateHMAC(dataHmac, key, algorithm);
        return pSign.toUpperCase();
    }

}
