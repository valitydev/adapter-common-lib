package com.rbkmoney.adapter.common.serializer;

public interface Serializer<TState> {

    byte[] writeByte(Object obj);

    String writeString(Object obj);

    TState read(byte[] data);

    TState read(String data);

}
