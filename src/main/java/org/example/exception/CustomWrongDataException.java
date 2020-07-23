package org.example.exception;

public class CustomWrongDataException extends RuntimeException {
    public CustomWrongDataException(String message) {
        super(message);
    }
}
