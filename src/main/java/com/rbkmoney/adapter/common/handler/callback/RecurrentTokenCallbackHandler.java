package com.rbkmoney.adapter.common.handler.callback;

import com.rbkmoney.adapter.common.enums.Step;
import com.rbkmoney.adapter.common.model.AdapterContext;
import com.rbkmoney.adapter.common.model.Callback;
import com.rbkmoney.adapter.common.properties.CommonTimerProperties;
import com.rbkmoney.adapter.common.serializer.AdapterSerializer;
import com.rbkmoney.adapter.common.serializer.CallbackSerializer;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenCallbackResult;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenContext;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenIntent;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenProxyResult;
import com.rbkmoney.java.damsel.utils.extractors.OptionsExtractors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static com.rbkmoney.java.damsel.utils.creators.BasePackageCreators.createTimerTimeout;
import static com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators.createRecurrentTokenCallbackResult;
import static com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators.createSleepIntent;

@Slf4j
@RequiredArgsConstructor
public class RecurrentTokenCallbackHandler implements CallbackHandler<RecurrentTokenCallbackResult, RecurrentTokenContext> {

    private final AdapterSerializer adapterSerializer;

    private final CallbackSerializer callbackSerializer;

    private final CommonTimerProperties timerProperties;

    @Override
    public RecurrentTokenCallbackResult handleCallback(ByteBuffer callback, RecurrentTokenContext context) {
        AdapterContext adapterContext = adapterSerializer.getAdapterContext(context);
        adapterContext.setStep(Step.GENERATE_TOKEN_FINISH_THREE_DS);
        Callback callbackObj = callbackSerializer.read(callback.array());
        adapterContext.setPaRes(callbackObj.getPaRes());
        adapterContext.setMd(callbackObj.getMd());

        int timerPollingDelay =
                OptionsExtractors.extractPollingDelay(context.getOptions(), timerProperties.getPollingDelay());
        byte[] callbackResponse = new byte[0];
        return createRecurrentTokenCallbackResult(
                callbackResponse,
                new RecurrentTokenProxyResult()
                        .setIntent(RecurrentTokenIntent.sleep(createSleepIntent(createTimerTimeout(timerPollingDelay))))
                        .setNextState(adapterSerializer.writeByte(adapterContext))
        );
    }
}