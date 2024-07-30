package org.finder.path.service;

public interface Processor<T> {
    T process(Object map);
}
