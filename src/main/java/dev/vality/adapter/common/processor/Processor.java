package dev.vality.adapter.common.processor;

public interface Processor<R, T, E> {

    R process(T response, E context);

}
