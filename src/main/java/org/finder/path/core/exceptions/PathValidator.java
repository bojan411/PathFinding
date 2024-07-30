package org.finder.path.core.exceptions;

public interface PathValidator<T> {
    T validate() throws ValidationError;
}
