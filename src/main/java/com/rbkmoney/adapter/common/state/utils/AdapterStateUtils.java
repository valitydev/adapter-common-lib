package com.rbkmoney.adapter.common.state.utils;

import com.rbkmoney.adapter.common.model.AdapterContext;
import com.rbkmoney.adapter.common.state.deserializer.AdapterDeserializer;
import com.rbkmoney.damsel.proxy_provider.PaymentContext;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdapterStateUtils {

    public static AdapterContext getAdapterContext(Object context, AdapterDeserializer adapterDeserializer) {
        AdapterContext adapterContext = new AdapterContext();
        byte[] state = getState(context);
        if (state != null && state.length > 0) {
            return adapterDeserializer.read(state);
        }
        return adapterContext;
    }

    private static byte[] getState(Object context) {
        if (context instanceof RecurrentTokenContext) {
            if (((RecurrentTokenContext) context).getSession() == null) {
                return new byte[0];
            }
            return ((RecurrentTokenContext) context).getSession().getState();
        }
        return ((PaymentContext) context).getSession().getState();
    }

}
