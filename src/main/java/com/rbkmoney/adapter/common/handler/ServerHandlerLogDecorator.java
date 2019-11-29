package com.rbkmoney.adapter.common.handler;

import com.rbkmoney.adapter.common.utils.converter.PaymentResourceTypeResolver;
import com.rbkmoney.damsel.proxy_provider.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.nio.ByteBuffer;

import static com.rbkmoney.java.damsel.utils.extractors.ProxyProviderPackageExtractors.*;
import static com.rbkmoney.java.damsel.utils.verification.ProxyProviderVerification.isUndefinedResultOrUnavailable;

@Slf4j
@RequiredArgsConstructor
public class ServerHandlerLogDecorator implements ProviderProxySrv.Iface {

    private final ProviderProxySrv.Iface handler;

    @Override
    public RecurrentTokenProxyResult generateToken(RecurrentTokenContext context) throws TException {
        String recurrentId = extractRecurrentId(context);
        log.info("Generate token started with recurrentId {}", recurrentId);
        try {
            RecurrentTokenProxyResult proxyResult = handler.generateToken(context);
            log.info("Generate token finished {} with recurrentId {}", proxyResult, recurrentId);
            return proxyResult;
        } catch (Exception ex) {
            String message = "Failed handle generate token with recurrentId " + recurrentId;
            logMessage(ex, message);
            throw ex;
        }
    }

    @Override
    public RecurrentTokenCallbackResult handleRecurrentTokenCallback(ByteBuffer byteBuffer,
                                                                     RecurrentTokenContext context) throws TException {
        String recurrentId = extractRecurrentId(context);
        log.info("handleRecurrentTokenCallback: start with recurrentId {}", recurrentId);
        RecurrentTokenCallbackResult result = handler.handleRecurrentTokenCallback(byteBuffer, context);
        log.info("handleRecurrentTokenCallback end {} with recurrentId {}", result, recurrentId);
        return result;
    }

    @Override
    public PaymentProxyResult processPayment(PaymentContext context) throws TException {
        String invoiceId = extractInvoiceId(context);
        String invoicePaymentStatus = extractTargetInvoicePaymentStatus(context);
        String paymentResourceType = PaymentResourceTypeResolver.extractPaymentResourceType(context);
        log.info("Process payment handle paymentResourceType='{}', invoicePaymentStatus='{}' start with invoiceId {}", paymentResourceType, invoicePaymentStatus, invoiceId);
        try {
            PaymentProxyResult proxyResult = handler.processPayment(context);
            log.info("Process payment handle paymentResourceType='{}', invoicePaymentStatus='{}' finished with invoiceId {} and proxyResult {}",
                    paymentResourceType, invoicePaymentStatus, invoiceId, proxyResult);
            return proxyResult;
        } catch (Exception e) {
            String message = String.format("Failed handle paymentResourceType=%s, invoicePaymentStatus=%s process payment for operation with invoiceId %s",
                    paymentResourceType, invoicePaymentStatus, invoiceId);
            logMessage(e, message);
            throw e;
        }
    }

    @Override
    public PaymentCallbackResult handlePaymentCallback(ByteBuffer byteBuffer,
                                                       PaymentContext context) throws TException {
        String invoiceId = extractInvoiceId(context);
        log.info("handlePaymentCallback start with invoiceId {}", invoiceId);
        PaymentCallbackResult result = handler.handlePaymentCallback(byteBuffer, context);
        log.info("handlePaymentCallback finish {} with invoiceId {}", result, invoiceId);
        return result;
    }

    private static void logMessage(Exception ex, String message) {
        if (isUndefinedResultOrUnavailable(ex)) {
            log.warn(message, ex);
        } else {
            log.error(message, ex);
        }
    }
}
