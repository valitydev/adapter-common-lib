package com.rbkmoney.adapter.common.controller;

import com.rbkmoney.adapter.common.model.Callback;
import com.rbkmoney.adapter.common.serializer.CallbackSerializer;
import com.rbkmoney.adapter.helpers.hellgate.HellgateAdapterClient;
import com.rbkmoney.adapter.helpers.hellgate.exception.HellgateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;

@Slf4j
@RequiredArgsConstructor
public abstract class AdapterController {

    private final HellgateAdapterClient hgClient;

    private final CallbackSerializer callbackSerializer;

    public String receivePaymentIncomingParameters(HttpServletRequest servletRequest,
                                                   HttpServletResponse servletResponse) throws IOException {
        return processCallback(servletRequest, servletResponse, hgClient::processPaymentCallback);
    }

    public String receiveRecurrentIncomingParameters(HttpServletRequest servletRequest,
                                                     HttpServletResponse servletResponse) throws IOException {
        return processCallback(servletRequest, servletResponse, hgClient::processRecurrentTokenCallback);
    }

    private String processCallback(HttpServletRequest servletRequest,
                                   HttpServletResponse servletResponse,
                                   BiFunction<String, ByteBuffer, ByteBuffer> hgFunction) throws IOException {
        String resp = "";
        Callback callbackObj = callbackSerializer.read(servletRequest);
        log.info("ProcessCallback {}", callbackObj);
        try {
            String tag = callbackObj.getMd();
            ByteBuffer callback = ByteBuffer.wrap(callbackSerializer.writeByte(callbackObj));
            ByteBuffer response = hgFunction.apply(tag, callback);
            resp = new String(response.array(), StandardCharsets.UTF_8);
        } catch (HellgateException e) {
            log.warn("Failed handle callback for recurrent", e);
        } catch (Exception e) {
            log.error("Failed handle callback for recurrent", e);
        }
        if (StringUtils.hasText(callbackObj.getTerminationUri())) {
            servletResponse.sendRedirect(callbackObj.getTerminationUri());
        }
        log.info("ProcessCallback response {}", resp);
        return resp;
    }

}
