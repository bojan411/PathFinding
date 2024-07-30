package org.finder.path.core.exceptions.validation;

import org.finder.path.core.exceptions.ValidationError;

public class MultiplePossibleDirectionsError extends ValidationError {
    public MultiplePossibleDirectionsError(final String message) {
        super(message);
    }
}
