package dev.vality.adapter.common.exception;

/**
 * @deprecated Use {@code vault-client}. This API will be removed in the next major release.
 */
@Deprecated(forRemoval = true)
public class HexDecodeException extends RuntimeException {
    public HexDecodeException(String message) {
        super("Secret must be in hex-format: " + message);
    }
}
