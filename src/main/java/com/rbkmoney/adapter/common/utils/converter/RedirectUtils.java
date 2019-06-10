package com.rbkmoney.adapter.common.utils.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class RedirectUtils {

    public static String getCallbackUrl(String callbackUrl, String path) {
        return UriComponentsBuilder.fromUriString(callbackUrl)
                .path(path)
                .build()
                .toUriString();
    }
}
