package org.finder.path.core;

import java.util.List;

public interface Finder<T, U> {
    List<T> get(DirectionConfig conf);
    T determineNextStep(T currentStep);
    List<U> lookAround(T current);
    U lookRight(T current);
    U lookLeft(T current);
    U lookUp(T current);
    U lookDown(T current);

}
