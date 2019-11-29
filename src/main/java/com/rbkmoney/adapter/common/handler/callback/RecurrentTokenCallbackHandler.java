package com.rbkmoney.adapter.common.handler.callback;

import com.rbkmoney.adapter.common.enums.Step;
import com.rbkmoney.adapter.common.model.AdapterContext;
import com.rbkmoney.adapter.common.model.Callback;
import com.rbkmoney.adapter.common.state.deserializer.AdapterDeserializer;
import com.rbkmoney.adapter.common.state.deserializer.CallbackDeserializer;
import com.rbkmoney.adapter.common.state.serializer.AdapterSerializer;
import com.rbkmoney.adapter.common.state.utils.AdapterStateUtils;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenCallbackResult;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenContext;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenIntent;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenProxyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static com.rbkmoney.java.damsel.utils.creators.BasePackageCreators.createTimerTimeout;
import static com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators.createRecurrentTokenCallbackResult;
import static com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators.createSleepIntent;

@Slf4j
@RequiredArgsConstructor
public class RecurrentTokenCallbackHandler implements CallbackHandler<RecurrentTokenCallbackResult, RecurrentTokenContext> {

    private final AdapterDeserializer adapterDeserializer;
    private final AdapterSerializer adapterSerializer;
    private final CallbackDeserializer callbackDeserializer;

    @Override
    public RecurrentTokenCallbackResult handleCallback(ByteBuffer callback, RecurrentTokenContext context) {
        AdapterContext adapterContext = AdapterStateUtils.getAdapterContext(context, adapterDeserializer);
        adapterContext.setStep(Step.GENERATE_TOKEN_FINISH_THREE_DS);
        Callback callbackObj = callbackDeserializer.read(callback.array());
        adapterContext.setPaRes(callbackObj.getPaRes());
        adapterContext.setMd(callbackObj.getMd());

        byte[] callbackResponse = new byte[0];
        return createRecurrentTokenCallbackResult(
                callbackResponse,
                new RecurrentTokenProxyResult()
                        .setIntent(RecurrentTokenIntent.sleep(createSleepIntent(createTimerTimeout(0))))
                        .setNextState(adapterSerializer.writeByte(adapterContext))
        );
    }
}