package dev.vality.adapter.common.state.deserializer;

public interface Deserializer<T> {

    T read(byte[] data);

    T read(String data);

}
