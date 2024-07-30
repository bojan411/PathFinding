package org.finder.path.core;

import org.finder.path.Direction;
import org.finder.path.core.exceptions.ValidationError;
import org.finder.path.core.impl.Path;
import org.finder.path.core.model.PathCharacter;
import org.finder.path.core.model.ResolvedPath;
import org.finder.path.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Path resolving interface.
 * Resolves path to ResolvedPath instance based on input map.
 *
 * @author bojan.kljucevic
 */
public abstract class PathResolver implements InputParser<PathCharacter> {

    /**
     * Logger.
     */
    private Logger logger;

    /**
     * Input map as String.
     */
    private List<String> parsedMap = new ArrayList<>();

    public PathResolver(final HashMap<Direction, String> config, final Logger logger) {
        if (config == null) {
            final HashMap<Direction, String> conf = new HashMap<>();
            DirectionConfig.configure(conf);
        } else {
            DirectionConfig.configure(config);
        }

        this.logger = logger;
    }

    /**
     * Resolving method that returns resolved currentPath as string and containing letters.
     * @param inputMap
     *        Any input provided by InputParser interface.
     * @return ResolvedPath describing resolved currentPath as string and containing letters
     */
    public ResolvedPath resolve(final Object inputMap) throws ValidationError {
        final List<PathCharacter> map = this.parseInput(inputMap);
        if (map == null || map.isEmpty()) {
            return new ResolvedPath("", "");
        }

        setParsedMap(map);
        final Path path = new Path(map, this.logger).validate();
        return path.move();
    }

    public Logger getLogger() {
        return logger;
    }

    public List<String> getParsedMap() {
        return parsedMap;
    }

    private void setParsedMap(final List<PathCharacter> pathChars) {
        String currentLine = "";
        for (PathCharacter pChar : pathChars) {
            currentLine = currentLine.concat(pChar.value());
            if ((pChar.index() + 1) % (Integer) DirectionConfig.valueOfDirectionConfig(Direction.ITEMS_PER_GROUP) == 0) {
                this.parsedMap.add(currentLine);
                currentLine = "";
            }
        }
    }

    public HashMap<Direction, Object> getConfig() {
        return DirectionConfig.getDirectionConfig();
    }

    public void setConfig(final Direction direction, final Object value) {
        DirectionConfig.getDirectionConfig().replace(direction, value);
    }
}
