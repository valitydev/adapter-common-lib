package dev.vality.adapter.common.state.serializer;

public interface Serializer<T> {

    byte[] writeByte(T obj);

    String writeString(T obj);

}
