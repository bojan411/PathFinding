package org.finder.path.args;

public enum Arguments {
    /**
     * No value represented in current path.
     */
    NONE("-n", "Represents empty[none] value in path.", "\" \""),

    /**
     * Up or down value represented in current path.
     */
    UP_OR_DOWN("-ud", "Represents value used to go up or down.", "|"),

    /**
     * Left or right value represented in current path.
     */
    LEFT_OR_RIGHT("-lr", "Represents value used to go left or right.", "-"),

    /**
     * Starting value in current path.
     */
    START("-s", "Represents starting value.", "@"),

    /**
     * Ending value in current path.
     */
    END("-e", "Represents ending value.", "x"),

    /**
     * Intersection value in current path.
     */
    INTERSECTION("-i", "Represents intersection value.", "+ or any letter"),

    /**
     * Char that is INTERSECTION but is used for validation.
     */
    INTERSECTION_SPECIAL_CHAR("-isc", "Represents intersection value on which to validate errors like fork and fake turn.", "+"),

    /**
     * Number of items per list in map.
     */
    ITEMS_PER_GROUP("-ipg", "Represents the maximum number of items are in each map list.", "2"),

    /**
     * Where to find current path.
     */
    PATH_LOCATION("-f", "Represents where to find currentPath to traverse.", "N/A"),

    /**
     * Show help.
     */
    HELP("-h", "Represents help menu with described arguments.", "N/A");

    private String argumentName;
    private String help;
    private String defaultValue;

    Arguments(final String argumentName, final String help, final String defaultValue) {
        this.argumentName = argumentName;
        this.help = help;
        this.defaultValue = defaultValue;
    }

    public static Arguments valueOfArg(final String argument) {
        for (Arguments arg : values()) {
            if (arg.argumentName.equals(argument)) {
                return arg;
            }
        }
        return null;
    }

    @SuppressWarnings("checkstyle:Regexp")
    public static void getHelp() {
        String helpText = "Look bellow arguments for help. N/A = Mandatory argument): \n\n";
        for (Arguments arg : values()) {
            helpText = helpText.concat("\t ");
            helpText = helpText.concat(arg.argumentName);
            helpText = helpText.concat(" ");
            helpText = helpText.concat("[" + arg.help + "]");
            helpText = helpText.concat(" ");
            helpText = arg != HELP ? helpText.concat("[DEFAULT: " + arg.defaultValue + "]") : helpText;
            helpText = helpText.concat("\n");
        }

        System.out.println(helpText);
    }
}
