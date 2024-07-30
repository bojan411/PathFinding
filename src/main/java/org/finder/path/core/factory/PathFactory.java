package org.finder.path.core.factory;

public abstract class PathFactory<T> {
    public T create(final Object... argumenst) {
        return createInstance(argumenst);
    }
    protected abstract T createInstance(Object... argumenst);
}
