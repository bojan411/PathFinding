package org.finder.path.core.exceptions;

public class ValidationError extends RuntimeException {
    public ValidationError(final String message) {
        super(message);
    }
}
