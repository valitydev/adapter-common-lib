package dev.vality.adapter.common.handler.callback;

import java.nio.ByteBuffer;

public interface CallbackHandler<T, R> {

    T handleCallback(ByteBuffer byteBuffer, R context);

}
