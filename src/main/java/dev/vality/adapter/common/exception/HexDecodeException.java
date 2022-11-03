package dev.vality.adapter.common.exception;

public class HexDecodeException extends RuntimeException {
    public HexDecodeException(String message) {
        super("Key must be in hex-format: " + message);
    }
}
