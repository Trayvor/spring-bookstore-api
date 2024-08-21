package org.example.bookstore.exception;

public class NoValidationFieldsException extends RuntimeException {
    public NoValidationFieldsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
