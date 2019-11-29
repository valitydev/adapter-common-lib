package com.rbkmoney.adapter.common.state.deserializer;

public interface Deserializer<TDState> {

    TDState read(byte[] data);

    TDState read(String data);

}
