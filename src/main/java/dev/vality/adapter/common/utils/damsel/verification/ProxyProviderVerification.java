package dev.vality.adapter.common.utils.damsel.verification;

import dev.vality.damsel.proxy_provider.*;
import dev.vality.woody.api.flow.error.WErrorDefinition;
import dev.vality.woody.api.flow.error.WErrorType;
import dev.vality.woody.api.flow.error.WRuntimeException;
import dev.vality.woody.api.trace.context.TraceContext;
import dev.vality.woody.thrift.impl.http.error.THTransportErrorMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProxyProviderVerification {

    public static boolean isSuccess(PaymentProxyResult proxyResult) {
        return proxyResult.getIntent().getFinish().getStatus().isSetSuccess();
    }

    public static boolean isSuccess(PaymentCallbackResult callbackResult) {
        return callbackResult.getResult().getIntent().getFinish().getStatus().isSetSuccess();
    }

    public static boolean isSuccess(RecurrentTokenProxyResult proxyResult) {
        return proxyResult.getIntent().getFinish().getStatus().isSetSuccess();
    }

    public static boolean isSuccess(RecurrentTokenCallbackResult callbackResult) {
        return callbackResult.getResult().getIntent().getFinish().getStatus().isSetSuccess();
    }

    public static boolean isSuspend(PaymentProxyResult proxyResult) {
        return proxyResult.getIntent().isSetSuspend();
    }

    public static boolean isSuspend(RecurrentTokenProxyResult proxyResult) {
        return proxyResult.getIntent().isSetSuspend();
    }

    public static boolean isSuspend(RecurrentTokenCallbackResult callbackResult) {
        return callbackResult.getResult().getIntent().isSetSuspend();
    }

    public static boolean isSleep(PaymentProxyResult proxyResult) {
        return proxyResult.getIntent().isSetSleep();
    }

    public static boolean isSleep(PaymentCallbackResult proxyResult) {
        return proxyResult.getResult().getIntent().isSetSleep();
    }

    public static boolean isSleep(RecurrentTokenProxyResult proxyResult) {
        return proxyResult.getIntent().isSetSleep();
    }

    public static boolean isFailure(PaymentProxyResult proxyResult) {
        return proxyResult.getIntent().getFinish().getStatus().isSetFailure();
    }

    public static boolean isFailure(PaymentCallbackResult callbackResult) {
        return callbackResult.getResult().getIntent().getFinish().getStatus().isSetFailure();
    }

    public static boolean isMakeRecurrent(PaymentContext context) {
        return (context.getPaymentInfo().getPayment().isSetMakeRecurrent()
                && context.getPaymentInfo().getPayment().isMakeRecurrent());
    }

    public static boolean isRecurrent(PaymentContext context) {
        return context.getPaymentInfo().getPayment().getPaymentResource().isSetRecurrentPaymentResource();
    }

    public static boolean isUndefinedResultOrUnavailable(Exception exception) {
        WErrorDefinition definition;
        if (exception instanceof WRuntimeException) {
            definition = ((WRuntimeException) exception).getErrorDefinition();
        } else {
            THTransportErrorMapper errorMapper = new THTransportErrorMapper();
            definition = errorMapper.mapToDef(exception, TraceContext.getCurrentTraceData().getActiveSpan());
        }

        boolean undefined = (definition != null
                && WErrorType.UNDEFINED_RESULT.getKey().equals(definition.getErrorType().getKey()));
        boolean unavailable = (definition != null
                && WErrorType.UNAVAILABLE_RESULT.getKey().equals(definition.getErrorType().getKey()));

        return (undefined || unavailable);
    }

}
