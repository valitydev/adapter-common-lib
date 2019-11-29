package com.rbkmoney.adapter.common.utils.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostBodyConverter {

    public static String getUrlEncodedString(MultiValueMap<String, String> paramsMap) {
        FormHttpMessageConverter converter = new FormHttpMessageConverter();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HttpOutputMessage outputMessage = prepareOutputMessage(os);
        try {
            converter.write(paramsMap, MediaType.APPLICATION_FORM_URLENCODED, outputMessage);
            return os.toString();
        } catch (IOException e) {
            throw new ConverterException(e);
        }
    }

    private static HttpOutputMessage prepareOutputMessage(ByteArrayOutputStream os) {
        return new HttpOutputMessage() {
            @Override
            public HttpHeaders getHeaders() {
                return new HttpHeaders();
            }

            @Override
            public OutputStream getBody() {
                return os;
            }
        };
    }
}
