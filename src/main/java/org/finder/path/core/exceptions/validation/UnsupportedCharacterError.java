package org.finder.path.core.exceptions.validation;

import org.finder.path.core.exceptions.ValidationError;

public class UnsupportedCharacterError extends ValidationError {
    public UnsupportedCharacterError(final String message) {
        super(message);
    }
}
