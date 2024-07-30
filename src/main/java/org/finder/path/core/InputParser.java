package org.finder.path.core;

import java.util.List;

public interface InputParser<T> {
    /**
     * Parsing method that takes input and creates input as list of ordered objects
     * indexed from 0-inputParameters.length().
     *
     * @param inputMap
     *        Any form of input.
     * @return List<T> describing each input character from inputMap
     */
    List<T> parseInput(Object inputMap);
}
