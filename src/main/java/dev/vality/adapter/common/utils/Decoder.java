package dev.vality.adapter.common.utils;

import jakarta.xml.bind.DatatypeConverter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Decoder {

    public static String base64DecodeAndGetHexString(String str) {
        byte[] bytes = java.util.Base64.getDecoder().decode(str.getBytes());
        return DatatypeConverter.printHexBinary(bytes);
    }

}
