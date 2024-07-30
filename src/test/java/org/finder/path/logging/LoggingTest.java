package org.finder.path.logging;

import org.finder.path.adapter.FilePathResolver;
import org.finder.path.logging.impl.ConsoleLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.finder.path.Direction;
import org.finder.path.core.DirectionConfig;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyString;

public class LoggingTest {

    @BeforeEach
    public void setup() {
        DirectionConfig.reset();
    }

    @Test
    public void testLoggerCalled() {
        final HashMap<Direction, String> config = new HashMap<>();
        final ConsoleLogger mockLogger = mock(ConsoleLogger.class);

        final FilePathResolver fileResolver = new FilePathResolver(config, mockLogger);
        fileResolver.resolve(null);

        verify(mockLogger, times(1)).logInfo(anyString());
    }
}
