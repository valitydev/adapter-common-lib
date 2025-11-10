package dev.vality.adapter.common.exception;

/**
 * @deprecated Use {@code vault-client}. This API will be removed in the next major release.
 */
@Deprecated(forRemoval = true)
public class SecretNotFoundException extends RuntimeException {
    public SecretNotFoundException(String message) {
        super(message);
    }
}
