package org.finder.path.core.exceptions.validation;

import org.finder.path.core.exceptions.ValidationError;

public class DuplicateDirectionValueError extends ValidationError {
    public DuplicateDirectionValueError(final String message) {
        super(message);
    }
}
