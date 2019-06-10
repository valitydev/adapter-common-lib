package com.rbkmoney.adapter.common.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.common.model.AdapterContext;
import com.rbkmoney.damsel.proxy_provider.PaymentContext;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenContext;

import java.io.IOException;

public class AdapterSerializer extends StateSerializer<AdapterContext> {

    public AdapterSerializer(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public AdapterContext read(byte[] data) {
        if (data == null) {
            return new AdapterContext();
        }
        try {
            return getMapper().readValue(data, AdapterContext.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public AdapterContext getAdapterContext(Object context) {
        AdapterContext adapterContext = new AdapterContext();
        byte[] state = getState(context);
        if (state != null && state.length > 0) {
            return this.read(state);
        }
        return adapterContext;
    }

    public static byte[] getState(Object context) {
        if (context instanceof RecurrentTokenContext) {
            if (((RecurrentTokenContext) context).getSession() == null) {
                return new byte[0];
            }
            return ((RecurrentTokenContext) context).getSession().getState();
        }
        return ((PaymentContext) context).getSession().getState();
    }

}
