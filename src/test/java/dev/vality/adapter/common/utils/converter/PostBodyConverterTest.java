package dev.vality.adapter.common.utils.converter;

import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostBodyConverterTest {

    @Test
    public void test() {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("kek", "lol");
        paramsMap.add("privet", "poka vsem");
        String urlEncodedString = PostBodyConverter.getUrlEncodedString(paramsMap);
        assertEquals("kek=lol&privet=poka+vsem", urlEncodedString);
    }
}
