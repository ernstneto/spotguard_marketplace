package com.spotguard.marketplace.core.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String field, Object value) {
        super("%s não encontrado(a) com %s: %s".formatted(resourceName, field, value));
    }
}
