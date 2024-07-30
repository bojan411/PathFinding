package org.finder.path.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.finder.path.core.DirectionConfig;
import org.finder.path.logging.impl.ConsoleLogger;

import java.util.HashMap;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.spy;


public class AdapterTest {

    @BeforeEach
    public void setup() {
        DirectionConfig.reset();
    }

    @ParameterizedTest
    @CsvSource({
            "src//test//resources//basic.txt",
    })
    public void testAdapterCalledWithCorrectParam(final String pathToInputFile) {
        final FilePathResolver mockResolver = new FilePathResolver(new HashMap<>(), new ConsoleLogger());
        final FilePathResolver spyMockResolver = spy(mockResolver);
        spyMockResolver.resolve(pathToInputFile);

        verify(spyMockResolver, times(1)).parseInput(pathToInputFile);
    }
}
