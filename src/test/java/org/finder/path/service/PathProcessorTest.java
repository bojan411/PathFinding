package org.finder.path.service;

import org.finder.path.service.impl.PathProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.finder.path.adapter.FilePathResolver;
import org.finder.path.core.DirectionConfig;
import org.finder.path.core.PathResolver;
import org.finder.path.core.model.ResolvedPath;
import org.finder.path.core.exceptions.ValidationError;
import org.finder.path.logging.impl.ConsoleLogger;

import java.util.HashMap;

public class PathProcessorTest {

    @BeforeEach
    public void setup() {
        DirectionConfig.reset();
    }

    @ParameterizedTest
    @MethodSource("org.finder.path.DataProvider#getHappyPathData")
    public void testPaths(final String pathToInputFile, final String expectedLetters, final String expectedPath) {
        final PathProcessor processor = new PathProcessor(new FilePathResolver(new HashMap<>(), new ConsoleLogger()));
        final ResolvedPath res = processor.process(pathToInputFile);

        Assertions.assertEquals(expectedLetters, res.letters());
        Assertions.assertEquals(expectedPath, res.pathAsString());
    }

    @ParameterizedTest
    @MethodSource("org.finder.path.DataProvider#getErrorPathData")
    public void testPathsOnError(final String pathToInputFile) {
        Assertions.assertThrows(ValidationError.class, () -> {
            final PathResolver pathResolver = new FilePathResolver(new HashMap<>(), new ConsoleLogger());
            pathResolver.resolve(pathToInputFile);
        });
    }
}
