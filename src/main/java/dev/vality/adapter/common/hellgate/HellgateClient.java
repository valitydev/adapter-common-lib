package dev.vality.adapter.common.hellgate;

import dev.vality.adapter.common.exception.HellgateException;
import dev.vality.damsel.proxy_provider.ProviderProxyHostSrv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.nio.ByteBuffer;

@Slf4j
@RequiredArgsConstructor
public class HellgateClient {

    private final ProviderProxyHostSrv.Iface providerProxyHostSrv;

    public ByteBuffer processPaymentCallback(String tag, ByteBuffer callback) {
        log.info("processPaymentCallback start with tag {}", tag);
        try {
            ByteBuffer callbackResponse = providerProxyHostSrv.processPaymentCallback(tag, callback);
            log.info("processPaymentCallback finish with tag {}", tag);
            return callbackResponse;
        } catch (TException ex) {
            throw new HellgateException(String.format("Exception in processPaymentCallback with tag: %s", tag), ex);
        }
    }

    public ByteBuffer processRecurrentTokenCallback(String tag, ByteBuffer callback) {
        log.info("processRecurrentTokenCallback start with tag {}", tag);
        try {
            ByteBuffer callbackResponse = providerProxyHostSrv.processRecurrentTokenCallback(tag, callback);
            log.info("processRecurrentTokenCallback finish with tag {}", tag);
            return callbackResponse;
        } catch (TException ex) {
            throw new HellgateException(String.format("Exception in processRecurrentTokenCallback with tag: %s", tag),
                    ex);
        }
    }

}
