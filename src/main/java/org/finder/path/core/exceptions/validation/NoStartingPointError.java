package org.finder.path.core.exceptions.validation;

import org.finder.path.core.exceptions.ValidationError;

public class NoStartingPointError extends ValidationError {
    public NoStartingPointError(final String message) {
        super(message);
    }
}
