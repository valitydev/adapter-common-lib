package com.rbkmoney.adapter.common.utils.encryption;

public class HmacEncryptionException extends RuntimeException {
    public HmacEncryptionException() {
        super();
    }

    public HmacEncryptionException(String message) {
        super(message);
    }

    public HmacEncryptionException(Throwable cause) {
        super(cause);
    }

    public HmacEncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
