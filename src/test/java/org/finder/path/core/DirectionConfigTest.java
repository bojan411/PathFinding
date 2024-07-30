package org.finder.path.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.finder.path.Direction;
import org.finder.path.core.exceptions.ValidationError;

import java.util.HashMap;

public class DirectionConfigTest {

    @BeforeEach
    public void setup() {
        DirectionConfig.reset();
    }

    @ParameterizedTest
    @MethodSource("org.finder.path.DataProvider#getDirectionConfigData")
    public void testDirectionConfigProperlyParsed(final Direction direction, final String value) {
        final HashMap<Direction, String> conf = new HashMap<>();
        conf.put(direction, value);
        DirectionConfig.configure(conf);
        final DirectionConfig dirConfValue = (DirectionConfig) DirectionConfig.valueOfDirectionConfig(direction);
        Assertions.assertTrue(dirConfValue.matches(value));
    }

    @ParameterizedTest
    @MethodSource("org.finder.path.DataProvider#getDuplicateDirectionConfigData")
    public void testDirectionConfigDuplicateThrowsError(final Direction direction, final String value) {
        Assertions.assertThrows(ValidationError.class, () -> {
            final HashMap<Direction, String> conf = new HashMap<>();
            conf.put(direction, value);
            DirectionConfig.configure(conf);
        });
    }
}
