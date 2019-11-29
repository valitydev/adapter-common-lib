package com.rbkmoney.adapter.common.state.serializer;

public interface Serializer<TState> {

    byte[] writeByte(TState obj);

    String writeString(TState obj);

}
