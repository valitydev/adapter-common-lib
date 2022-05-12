package dev.vality.adapter.common.handler.callback;

import dev.vality.adapter.common.enums.Step;
import dev.vality.adapter.common.model.AdapterContext;
import dev.vality.adapter.common.model.Callback;
import dev.vality.adapter.common.state.deserializer.AdapterDeserializer;
import dev.vality.adapter.common.state.deserializer.CallbackDeserializer;
import dev.vality.adapter.common.state.serializer.AdapterSerializer;
import dev.vality.adapter.common.state.utils.AdapterStateUtils;
import dev.vality.damsel.proxy_provider.PaymentCallbackProxyResult;
import dev.vality.damsel.proxy_provider.PaymentCallbackResult;
import dev.vality.damsel.proxy_provider.PaymentContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static dev.vality.adapter.common.utils.damsel.creators.ProxyProviderPackageCreators.createCallbackResult;
import static dev.vality.adapter.common.utils.damsel.creators.ProxyProviderPackageCreators.createIntentWithSleepIntent;

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
