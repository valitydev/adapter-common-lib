package com.rbkmoney.adapter.common.utils.encryption;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.springframework.util.MultiValueMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HmacEncryption {

    public static String calculateHMacSha1(String data, String key) {
        return calculateHMAC(data, key, HmacAlgorithms.HMAC_SHA_1.getName());
    }

    public static String calculateHMacSha256(String data, String key) {
        return calculateHMAC(data, key, HmacAlgorithms.HMAC_SHA_256.getName());
    }

    public static String calculateHMAC(String data, String hexEncodedKey, String algorithm) {
        try {
            byte[] decodedKey = Hex.decodeHex(hexEncodedKey.toCharArray());
            return calculateHMAC(data, decodedKey, algorithm);
        } catch (Exception ex) {
            throw new HmacEncryptionException(ex);
        }
    }

    public static String calculateHMAC(String data, byte[] key, String algorithm) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(keySpec);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] resultBytes = mac.doFinal(dataBytes);
            return new String(new Hex().encode(resultBytes));
        } catch (Exception ex) {
            throw new HmacEncryptionException(ex);
        }
    }

    public static String prepareDataForHmac(String[] fields, MultiValueMap<String, String> params) {
        StringBuilder dataHmac = new StringBuilder();
        Arrays.asList(fields).forEach(fillDataHmac(params, dataHmac));
        return dataHmac.toString();
    }

    private static Consumer<String> fillDataHmac(MultiValueMap<String, String> params, StringBuilder dataHmac) {
        return field -> {
                    if (hasFieldAndNotEmpty(params, field)) {
                        dataHmac.append(params.get(field).get(0).length());
                        dataHmac.append(params.get(field).get(0));
                    } else {
                        dataHmac.append("-");
                    }
                };
    }

    private static boolean hasFieldAndNotEmpty(MultiValueMap<String, String> params, String field) {
        return params.get(field) != null && !params.get(field).isEmpty()
                && params.get(field).get(0) != null && !params.get(field).get(0).isEmpty();
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
