package org.finder.path.core.exceptions.validation;

import org.finder.path.core.exceptions.ValidationError;

public class BrokenPathError extends ValidationError {
    public BrokenPathError(final String message) {
        super(message);
    }
}
