package dev.vality.adapter.common.exception;

/**
 * Handy class for wrapping runtime {@code Exceptions} with a root cause.
 */
public class HellgateException extends RuntimeException {

    /**
     * Construct a new {@code HellgateException} with the specified detail message.
     *
     * @param message the detail message
     */
    public HellgateException(String message) {
        super(message);
    }

    /**
     * Construct a new {@code HellgateException} with the cause.
     *
     * @param cause the root cause
     */
    public HellgateException(Throwable cause) {
        super(cause);
    }

    /**
     * Construct a new {@code HellgateException} with the
     * specified detail message and root cause.
     *
     * @param message the detail message
     * @param cause   the root cause
     */
    public HellgateException(String message, Throwable cause) {
        super(message, cause);
    }

}
