package org.finder.path.core.exceptions.validation;

import org.finder.path.core.exceptions.ValidationError;

public class MultipleEndingPointError extends ValidationError {
    public MultipleEndingPointError(final String message) {
        super(message);
    }
}
