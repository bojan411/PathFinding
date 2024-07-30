package org.finder.path.core.exceptions.validation;

import org.finder.path.core.exceptions.ValidationError;

public class MultipleStartingPathsError extends ValidationError {
    public MultipleStartingPathsError(final String message) {
        super(message);
    }
}
