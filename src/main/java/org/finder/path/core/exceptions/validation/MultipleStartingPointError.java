package org.finder.path.core.exceptions.validation;

import org.finder.path.core.exceptions.ValidationError;

public class MultipleStartingPointError extends ValidationError {
    public MultipleStartingPointError(final String message) {
        super(message);
    }
}
