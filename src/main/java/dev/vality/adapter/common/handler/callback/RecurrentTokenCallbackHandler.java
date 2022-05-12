package dev.vality.adapter.common.handler.callback;

import dev.vality.adapter.common.enums.Step;
import dev.vality.adapter.common.model.AdapterContext;
import dev.vality.adapter.common.model.Callback;
import dev.vality.adapter.common.state.deserializer.AdapterDeserializer;
import dev.vality.adapter.common.state.deserializer.CallbackDeserializer;
import dev.vality.adapter.common.state.serializer.AdapterSerializer;
import dev.vality.adapter.common.state.utils.AdapterStateUtils;
import dev.vality.damsel.proxy_provider.RecurrentTokenCallbackResult;
import dev.vality.damsel.proxy_provider.RecurrentTokenContext;
import dev.vality.damsel.proxy_provider.RecurrentTokenIntent;
import dev.vality.damsel.proxy_provider.RecurrentTokenProxyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static dev.vality.adapter.common.utils.damsel.creators.BasePackageCreators.createTimerTimeout;
import static dev.vality.adapter.common.utils.damsel.creators.ProxyProviderPackageCreators.createRecurrentTokenCallbackResult;
import static dev.vality.adapter.common.utils.damsel.creators.ProxyProviderPackageCreators.createSleepIntent;

@Slf4j
@RequiredArgsConstructor
public class RecurrentTokenCallbackHandler
        implements CallbackHandler<RecurrentTokenCallbackResult, RecurrentTokenContext> {

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