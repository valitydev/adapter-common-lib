package com.rbkmoney.adapter.common.handler.callback;

import com.rbkmoney.adapter.common.enums.Step;
import com.rbkmoney.adapter.common.model.AdapterContext;
import com.rbkmoney.adapter.common.model.Callback;
import com.rbkmoney.adapter.common.properties.CommonTimerProperties;
import com.rbkmoney.adapter.common.serializer.AdapterSerializer;
import com.rbkmoney.adapter.common.serializer.CallbackSerializer;
import com.rbkmoney.damsel.proxy_provider.PaymentCallbackProxyResult;
import com.rbkmoney.damsel.proxy_provider.PaymentCallbackResult;
import com.rbkmoney.damsel.proxy_provider.PaymentContext;
import com.rbkmoney.java.damsel.utils.extractors.OptionsExtractors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators.createCallbackResult;
import static com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators.createIntentWithSleepIntent;

@Slf4j
@RequiredArgsConstructor
public class PaymentCallbackHandler implements CallbackHandler<PaymentCallbackResult, PaymentContext> {

    private final AdapterSerializer adapterSerializer;

    private final CallbackSerializer callbackSerializer;

    private final CommonTimerProperties timerProperties;

    @Override
    public PaymentCallbackResult handleCallback(ByteBuffer callback, PaymentContext context) {
        AdapterContext adapterContext = adapterSerializer.getAdapterContext(context);
        adapterContext.setNextStep(Step.FINISH_THREE_DS);
        Callback callbackObj = callbackSerializer.read(callback.array());
        adapterContext.setPaRes(callbackObj.getPaRes());
        adapterContext.setMd(callbackObj.getMd());

        int timerPollingDelay =
                OptionsExtractors.extractPollingDelay(context.getOptions(), timerProperties.getPollingDelay());
        byte[] callbackResponse = new byte[0];
        return createCallbackResult(
                callbackResponse,
                new PaymentCallbackProxyResult()
                        .setIntent(createIntentWithSleepIntent(timerPollingDelay))
                        .setNextState(adapterSerializer.writeByte(adapterContext))
        );
    }
}
