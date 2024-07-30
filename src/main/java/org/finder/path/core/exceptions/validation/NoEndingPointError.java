package org.finder.path.core.exceptions.validation;

import org.finder.path.core.exceptions.ValidationError;

public class NoEndingPointError extends ValidationError {
    public NoEndingPointError(final String message) {
        super(message);
    }
}
