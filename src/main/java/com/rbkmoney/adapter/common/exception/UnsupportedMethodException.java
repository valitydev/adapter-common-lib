package com.rbkmoney.adapter.common.exception;

import org.apache.thrift.TException;

public class UnsupportedMethodException extends TException {

    public UnsupportedMethodException() {
        super("Unsupported method");
    }

}
