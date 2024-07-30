package org.finder.path.core;

import org.finder.path.Direction;
import org.finder.path.core.exceptions.PathValidator;
import org.finder.path.core.exceptions.ValidationError;
import org.finder.path.core.exceptions.validation.DuplicateDirectionValueError;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings({"checkstyle:StaticVariableName", "checkstyle:VisibilityModifier"})
public final class DirectionConfig implements PathValidator<DirectionConfig> {
    /**
     * No value represented in current path.
     */
    public static DirectionConfig NONE;

    /**
     * Up or down value represented in current path.
     */
    public static DirectionConfig UP_OR_DOWN;

    /**
     * Left or right value represented in current path.
     */
    public static DirectionConfig LEFT_OR_RIGHT;

    /**
     * Starting value in current path.
     */
    public static DirectionConfig START;

    /**
     * Ending value in current path.
     */
    public static DirectionConfig END;

    /**
     * Intersection value in current path.
     */
    public static DirectionConfig INTERSECTION;

    /**
     * Char that is INTERSECTION but is used for validation.
     */
    public static DirectionConfig INTERSECTION_SPECIAL_CHAR;

    /**
     * Number of items per list in map.
     */
    public static int ITEMS_PER_GROUP;

    private static HashMap<Direction, Object> DIRECTION_CONFIG = new HashMap<>();

    private final Pattern direction;

    private DirectionConfig(final Pattern directionValue) {
        direction = directionValue;
    }

    public static void configure(final HashMap<Direction, String> stepConfig) {
        NONE = new DirectionConfig(Pattern.compile(getStepConfigOrDefault(stepConfig, Direction.NONE, "^$|\s"))).validate();
        DIRECTION_CONFIG.put(Direction.NONE, NONE);
        UP_OR_DOWN = new DirectionConfig(Pattern.compile(getStepConfigOrDefault(stepConfig, Direction.UP_OR_DOWN, "\\|"))).validate();
        DIRECTION_CONFIG.put(Direction.UP_OR_DOWN, UP_OR_DOWN);
        LEFT_OR_RIGHT = new DirectionConfig(Pattern.compile(getStepConfigOrDefault(stepConfig, Direction.LEFT_OR_RIGHT, "-"))).validate();
        DIRECTION_CONFIG.put(Direction.LEFT_OR_RIGHT, LEFT_OR_RIGHT);
        START = new DirectionConfig(Pattern.compile(getStepConfigOrDefault(stepConfig, Direction.START, "@"))).validate();
        DIRECTION_CONFIG.put(Direction.START, START);
        END = new DirectionConfig(Pattern.compile(getStepConfigOrDefault(stepConfig, Direction.END, "x"))).validate();
        DIRECTION_CONFIG.put(Direction.END, END);
        INTERSECTION = new DirectionConfig(Pattern.compile(getStepConfigOrDefault(stepConfig, Direction.INTERSECTION, "(?![x])[a-zA-Z]"))).validate();
        DIRECTION_CONFIG.put(Direction.INTERSECTION, INTERSECTION);
        INTERSECTION_SPECIAL_CHAR = new DirectionConfig(Pattern.compile(getStepConfigOrDefault(stepConfig, Direction.INTERSECTION_SPECIAL_CHAR, "[\\+]"))).validate();
        DIRECTION_CONFIG.put(Direction.INTERSECTION_SPECIAL_CHAR, INTERSECTION_SPECIAL_CHAR);
        ITEMS_PER_GROUP = Integer.parseInt(getStepConfigOrDefault(stepConfig, Direction.ITEMS_PER_GROUP, "0"));
        DIRECTION_CONFIG.put(Direction.ITEMS_PER_GROUP, ITEMS_PER_GROUP);
    }

    public boolean matches(final String currentDirection) {
        return this.direction.matcher(currentDirection).find();
    }

    public String asString() {
        return direction.pattern();
    }

    public static Direction valueOfDirection(final String direction) {
        final Class clazz;
        try {
            clazz = Class.forName("org.finder.path.core.DirectionConfig");
        } catch (ClassNotFoundException e) {
            return Direction.NONE;
        }
        for (Field f : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(f.getModifiers())) {
                try {
                    if (((DirectionConfig)f.get(null)).matches(direction)) {
                        return Direction.valueOf(f.getName());
                    }
                } catch (IllegalAccessException e) {
                    return Direction.NONE;
                }
            }
        }

        return Direction.NONE;
    }

    public static Object valueOfDirectionConfig(final Direction direction) {
        return getDirectionConfig().entrySet().stream().filter( conf -> conf.getKey().equals(direction)).findFirst().get().getValue();
    }

    private static String getStepConfigOrDefault(final HashMap<Direction, String> stepConfig, final  Direction direction, final String defaultValue) {
        return stepConfig.get(direction) != null ? stepConfig.get(direction) : defaultValue;
    }

    public static HashMap<Direction, Object> getDirectionConfig() {
        return DIRECTION_CONFIG;
    }

    public static void reset() {
        DIRECTION_CONFIG = new HashMap<>();
    }

    @Override
    public DirectionConfig validate() throws ValidationError {
        DirectionConfig.getDirectionConfig().remove(Direction.ITEMS_PER_GROUP);
        final List<String> dirValues = getDirectionConfig().values().stream().map(val -> ((DirectionConfig)val).asString()).toList();
        if (!dirValues.stream().filter(dirVal -> Collections.frequency(dirValues, dirVal) > 1).collect(Collectors.toSet()).isEmpty()) {
            throw new DuplicateDirectionValueError("Same value can`t represent two path characters");
        }
        return this;
    }
}
