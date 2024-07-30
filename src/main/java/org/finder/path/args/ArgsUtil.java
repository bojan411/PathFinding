package org.finder.path.args;

import org.finder.path.Direction;
import org.finder.path.core.exceptions.validation.PathNotFoundError;

import java.util.HashMap;

public final class ArgsUtil {

    /**
     * CLI key and value default parameter range.
     */
    private static final Integer CLI_DEFAULT_PARAM_RANGE = 2;

    private ArgsUtil() { }


    public static HashMap<Arguments, HashMap<Direction, String>> parseArgs(final String[] inputArgs) {
        final HashMap<Arguments, HashMap<Direction, String>> args = new HashMap<>();
        for (int i = 0; i < inputArgs.length; i += CLI_DEFAULT_PARAM_RANGE) {
            final Arguments arg = Arguments.valueOfArg(inputArgs[i]);
            switch (arg) {
                case NONE:
                    args.put(Arguments.NONE, getDirectionValueMapping(Direction.NONE, inputArgs, i + 1));
                    break;
                case UP_OR_DOWN:
                    args.put(Arguments.UP_OR_DOWN, getDirectionValueMapping(Direction.UP_OR_DOWN, inputArgs, i + 1));
                    break;
                case LEFT_OR_RIGHT:
                    args.put(Arguments.LEFT_OR_RIGHT, getDirectionValueMapping(Direction.LEFT_OR_RIGHT, inputArgs, i + 1));
                    break;
                case START:
                    args.put(Arguments.START, getDirectionValueMapping(Direction.START, inputArgs, i + 1));
                    break;
                case END:
                    args.put(Arguments.END, getDirectionValueMapping(Direction.END, inputArgs, i + 1));
                    break;
                case INTERSECTION:
                    args.put(Arguments.INTERSECTION, getDirectionValueMapping(Direction.INTERSECTION, inputArgs, i + 1));
                    break;
                case INTERSECTION_SPECIAL_CHAR:
                    args.put(Arguments.INTERSECTION_SPECIAL_CHAR,
                            getDirectionValueMapping(Direction.INTERSECTION_SPECIAL_CHAR, inputArgs, i + 1));
                    break;
                case ITEMS_PER_GROUP:
                    args.put(Arguments.ITEMS_PER_GROUP, getDirectionValueMapping(Direction.ITEMS_PER_GROUP, inputArgs, i + 1));
                    break;
                case PATH_LOCATION:
                    args.put(Arguments.PATH_LOCATION, getDirectionValueMapping(null, inputArgs, i + 1));
                    break;
                case HELP:
                    if (args.isEmpty()) {
                        Arguments.getHelp();
                    }
                    return null;
                default:
                    throw new IllegalArgumentException();
            }
        }

        if (args.get(Arguments.PATH_LOCATION) == null) {
            throw new PathNotFoundError("Argument -f is mandatory.");
        }

        return args;
    }

    public static String getFileLocation(final HashMap<Arguments, HashMap<Direction, String>> arguments) {
        return arguments.get(Arguments.PATH_LOCATION).values().stream().findFirst().get();
    }

    public static HashMap<Direction, String> getPathConfig(final HashMap<Arguments, HashMap<Direction, String>> arguments) {
        final HashMap<Direction, String> pathConfig = new HashMap<>();
        arguments.forEach((key, value) -> {
            if (!(key.equals(Arguments.PATH_LOCATION))) {
                pathConfig.putAll(value);
            }
        });
        return pathConfig;
    }

    private static HashMap<Direction, String> getDirectionValueMapping(final Direction direction, final String[] inputArgs, final int index) {
        final HashMap<Direction, String> pathLocation = new HashMap<>();
        pathLocation.put(direction, getValueAt(inputArgs, index));
        return pathLocation;
    }

    private static String getValueAt(final String[] args, final int index) {
        return args.length > 0 ? args[index] : "";
    }
}
