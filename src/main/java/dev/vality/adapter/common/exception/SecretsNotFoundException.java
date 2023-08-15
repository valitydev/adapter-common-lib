package dev.vality.adapter.common.exception;

public class SecretsNotFoundException extends RuntimeException {
    public SecretsNotFoundException(String message) {
        super(message);
    }
}
