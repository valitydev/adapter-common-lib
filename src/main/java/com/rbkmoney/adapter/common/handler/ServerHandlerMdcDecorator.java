package com.rbkmoney.adapter.common.handler;

import com.rbkmoney.damsel.proxy_provider.*;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.slf4j.MDC;

import java.nio.ByteBuffer;

import static com.rbkmoney.adapter.common.logback.mdc.MdcContext.mdcPutContext;

@RequiredArgsConstructor
public class ServerHandlerMdcDecorator implements ProviderProxySrv.Iface {

    private final ProviderProxySrv.Iface serverHandlerLogDecorator;

    public RecurrentTokenProxyResult generateToken(RecurrentTokenContext recurrentTokenContext) throws TException {
        mdcPutContext(recurrentTokenContext);
        try {
            return serverHandlerLogDecorator.generateToken(recurrentTokenContext);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public RecurrentTokenCallbackResult handleRecurrentTokenCallback(ByteBuffer byteBuffer, RecurrentTokenContext recurrentTokenContext) throws TException {
        mdcPutContext(recurrentTokenContext);
        try {
            return serverHandlerLogDecorator.handleRecurrentTokenCallback(byteBuffer, recurrentTokenContext);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public PaymentProxyResult processPayment(PaymentContext paymentContext) throws TException {
        mdcPutContext(paymentContext);
        try {
            return serverHandlerLogDecorator.processPayment(paymentContext);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public PaymentCallbackResult handlePaymentCallback(ByteBuffer byteBuffer, PaymentContext paymentContext) throws TException {
        mdcPutContext(paymentContext);
        try {
            return serverHandlerLogDecorator.handlePaymentCallback(byteBuffer, paymentContext);
        } finally {
            MDC.clear();
        }
    }

}
