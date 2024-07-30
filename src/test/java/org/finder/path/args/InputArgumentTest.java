package org.finder.path.args;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.finder.path.Direction;
import org.finder.path.core.DirectionConfig;
import org.finder.path.core.exceptions.ValidationError;

import java.util.HashMap;

public class InputArgumentTest {

    @BeforeEach
    public void setup() {
        DirectionConfig.reset();
    }

    @ParameterizedTest
    @MethodSource("org.finder.path.DataProvider#getArgumentData")
    public void testInputArgumentProperlyParsed(final String argumentName, final String value, final Arguments argument) {
        final String[] inputParamMapping =
                !argument.equals(Arguments.PATH_LOCATION) ?
                        new String[] { argumentName, value, "-f", "File" } : new String[] { argumentName, value };
        final HashMap<Arguments, HashMap<Direction, String>> args = ArgsUtil.parseArgs(inputParamMapping);
        final HashMap<Direction, String> argumentValue = args.get(argument);

        if (!argument.equals(Arguments.PATH_LOCATION)) {
            final Direction directionValue = argumentValue.entrySet()
                    .stream()
                    .filter(direction -> direction.getKey() != null && direction.getKey().name().equals(argument.name()))
                    .findFirst()
                    .get()
                    .getKey();
            Assertions.assertEquals(argument.name(), directionValue.name());
        } else {
            final String argValue = argumentValue.entrySet().stream().filter(direction -> direction.getValue().equals(value)).findFirst().get().getValue();
            Assertions.assertEquals(value, argValue);
        }
    }

    @ParameterizedTest
    @CsvSource({"-n, None"})
    public void testInputArgumentNoPathLocationThrowsError(final String argumentName, final String value) {
        Assertions.assertThrows(ValidationError.class, () -> {
            final String[] inutParamMapping = new String[] { argumentName, value };
            ArgsUtil.parseArgs(inutParamMapping);
        });
    }

    @ParameterizedTest
    @CsvSource({"-f, File"})
    public void testInputArgumentGetFileLocation(final String argumentName, final String value) {
        final String[] inutParamMapping = new String[] { argumentName, value };
        final HashMap<Arguments, HashMap<Direction, String>> parsedArgs = ArgsUtil.parseArgs(inutParamMapping);
        final String location = ArgsUtil.getFileLocation(parsedArgs);
        Assertions.assertEquals(value, location);
    }
}
