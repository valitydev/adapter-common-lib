package dev.vality.adapter.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostBodyConverter {

    @SneakyThrows
    public static String getUrlEncodedString(MultiValueMap<String, String> paramsMap) {
        FormHttpMessageConverter converter = new FormHttpMessageConverter();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HttpOutputMessage outputMessage = prepareOutputMessage(os);
        converter.write(paramsMap, MediaType.APPLICATION_FORM_URLENCODED, outputMessage);
        return os.toString();
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
