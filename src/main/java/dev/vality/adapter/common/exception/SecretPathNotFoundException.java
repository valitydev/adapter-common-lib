package dev.vality.adapter.common.exception;

/**
 * @deprecated Use {@code vault-client}. This API will be removed in the next major release.
 */
@Deprecated(forRemoval = true)
public class SecretPathNotFoundException extends RuntimeException {
    public SecretPathNotFoundException(String message) {
        super(message);
    }
}
