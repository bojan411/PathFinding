package org.finder.path.core.exceptions.validation;

import org.finder.path.core.exceptions.ValidationError;

public class PathNotFoundError extends ValidationError {
    public PathNotFoundError(final String message) {
        super(message);
    }
}
