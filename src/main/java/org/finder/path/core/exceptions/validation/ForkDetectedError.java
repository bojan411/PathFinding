package org.finder.path.core.exceptions.validation;

import org.finder.path.core.exceptions.ValidationError;

public class ForkDetectedError extends ValidationError {
    public ForkDetectedError(final String message) {
        super(message);
    }
}
