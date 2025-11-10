package dev.vality.adapter.common.exception;

/**
 * @deprecated Use {@code vault-client}. This API will be removed in the next major release.
 */
@Deprecated(forRemoval = true)
public class SecretAlreadyModifyException extends RuntimeException {

    public static final String CAS_ERROR_MESSAGE = "check-and-set parameter did not match the current version";

    public SecretAlreadyModifyException(Throwable cause) {
        super(cause);
    }
}
