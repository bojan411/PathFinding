package org.finder.path.adapter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.finder.path.core.DirectionConfig;
import org.finder.path.core.model.PathCharacter;
import org.finder.path.logging.impl.ConsoleLogger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.HashMap;

public class FilePathResolverTest {

    @BeforeEach
    public void setup() {
        DirectionConfig.reset();
    }

    @ParameterizedTest
    @CsvSource({
            "src//test//resources//move.txt",
    })
    public void testFilePathResolverProperlyParsesFile(final String pathToInputFile) {
        final FilePathResolver resolver = new FilePathResolver(new HashMap<>(), new ConsoleLogger());
        final List<PathCharacter> input = resolver.parseInput(pathToInputFile);

        final List<String> pCharValues = input.stream().map(pChars -> pChars.value()).toList();
        final List<String> expectedValues = Arrays.asList("|", " ", "@", "-", "|", " ");

        IntStream.range(0, pCharValues.size())
                .forEach(idx -> {
                    Assertions.assertEquals(pCharValues.get(idx), expectedValues.get(idx));
                });
    }
}
