package com.rbkmoney.adapter.common.utils.converter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PostBodyConverter {

    public static String getUrlEncodedString(MultiValueMap<String, String> paramsMap){

        FormHttpMessageConverter converter = new FormHttpMessageConverter();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HttpOutputMessage outputMessage = new HttpOutputMessage() {
            @Override
            public HttpHeaders getHeaders() {
                return new HttpHeaders();
            }
            @Override
            public OutputStream getBody() {
                return os;
            }
        };
        try {
            converter.write(paramsMap, MediaType.APPLICATION_FORM_URLENCODED, outputMessage);
            return os.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
