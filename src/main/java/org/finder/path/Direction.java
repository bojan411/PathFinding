package org.finder.path;

public enum Direction {

    /**
     * No value represented in current path.
     */
    NONE,

    /**
     * Up or down value represented in current path.
     */
    UP_OR_DOWN,

    /**
     * Left or right value represented in current path.
     */
    LEFT_OR_RIGHT,

    /**
     * Starting value in current path.
     */
    START,

    /**
     * Ending value in current path.
     */
    END,

    /**
     * Intersection value in current path.
     */
    INTERSECTION,

    /**
     * Char that is INTERSECTION but is used for validation.
     */
    INTERSECTION_SPECIAL_CHAR,

    /**
     * Number of items per list in map.
     */
    ITEMS_PER_GROUP,
}
