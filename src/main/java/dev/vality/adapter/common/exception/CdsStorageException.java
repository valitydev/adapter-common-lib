package dev.vality.adapter.common.exception;

public class CdsStorageException extends RuntimeException {

    public CdsStorageException() {
        super();
    }

    public CdsStorageException(String message) {
        super(message);
    }

    public CdsStorageException(Throwable cause) {
        super(cause);
    }

    public CdsStorageException(String message, Throwable cause) {
        super(message, cause);
    }

}
