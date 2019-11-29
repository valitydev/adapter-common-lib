package com.rbkmoney.adapter.common.handler.callback;

import com.rbkmoney.adapter.common.enums.Step;
import com.rbkmoney.adapter.common.model.AdapterContext;
import com.rbkmoney.adapter.common.model.Callback;
import com.rbkmoney.adapter.common.state.deserializer.AdapterDeserializer;
import com.rbkmoney.adapter.common.state.deserializer.CallbackDeserializer;
import com.rbkmoney.adapter.common.state.serializer.AdapterSerializer;
import com.rbkmoney.adapter.common.state.utils.AdapterStateUtils;
import com.rbkmoney.damsel.proxy_provider.PaymentCallbackProxyResult;
import com.rbkmoney.damsel.proxy_provider.PaymentCallbackResult;
import com.rbkmoney.damsel.proxy_provider.PaymentContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators.createCallbackResult;
import static com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators.createIntentWithSleepIntent;

@Slf4j
@RequiredArgsConstructor
public class PaymentCallbackHandler implements CallbackHandler<PaymentCallbackResult, PaymentContext> {

    private final AdapterDeserializer adapterDeserializer;
    private final AdapterSerializer adapterSerializer;
    private final CallbackDeserializer callbackDeserializer;

    @Override
    public PaymentCallbackResult handleCallback(ByteBuffer callback, PaymentContext context) {
        AdapterContext adapterContext = AdapterStateUtils.getAdapterContext(context, adapterDeserializer);
        adapterContext.setStep(Step.FINISH_THREE_DS);
        Callback callbackObj = callbackDeserializer.read(callback.array());
        adapterContext.setPaRes(callbackObj.getPaRes());
        adapterContext.setMd(callbackObj.getMd());

        byte[] callbackResponse = new byte[0];
        return createCallbackResult(
                callbackResponse,
                new PaymentCallbackProxyResult()
                        .setIntent(createIntentWithSleepIntent(0))
                        .setNextState(adapterSerializer.writeByte(adapterContext))
        );
    }
}
