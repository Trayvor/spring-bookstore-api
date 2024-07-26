package org.example.bookstore.exception;

public class KeyNotFoundException extends RuntimeException {
    public KeyNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public KeyNotFoundException(String message) {
        super(message);
    }
}
