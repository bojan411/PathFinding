package org.finder.path.core.impl;

import org.finder.path.adapter.FilePathResolver;
import org.finder.path.core.PathUtil;
import org.finder.path.core.model.PathCharacter;
import org.finder.path.core.model.PathNode;
import org.finder.path.core.model.ResolvedPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.finder.path.Direction;
import org.finder.path.core.DirectionConfig;
import org.finder.path.core.Finder;
import org.finder.path.core.factory.impl.PathNodeFactory;
import org.finder.path.logging.impl.ConsoleLogger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

import static org.finder.path.core.Constants.BEFORE_CHAR;
import static org.finder.path.core.Constants.BEFORE_NODE;
import static org.finder.path.core.Constants.AFTER_CHAR;
import static org.finder.path.core.Constants.AFTER_NODE;


public class PathTest {
    /**
     * Basic test.
     *
     */
    @Test
    public void testPathResolvedPathEmptyIfNoMap() {
        final HashMap<Direction, String> config = new HashMap<>();

        final FilePathResolver fileResolver = new FilePathResolver(config, new ConsoleLogger());
        final ResolvedPath resPath = fileResolver.resolve(null);

        Assertions.assertEquals(resPath.pathAsString(), "");
        Assertions.assertEquals(resPath.letters(), "");
    }

    @ParameterizedTest
    @MethodSource("org.finder.path.DataProvider#getPathCharactersAndNodes")
    public void testPathFilterPreviousNode(final List<PathCharacter> pathChars, final List<PathNode> node) {
        final List<PathCharacter> filteredChars = PathUtil.filterOutPreviousNode(pathChars, node.get(1));
        pathChars.removeAll(filteredChars);
        Assertions.assertTrue(filteredChars.size() != pathChars.size());
        Assertions.assertTrue(Objects.equals(pathChars.stream().findFirst().get().value(), node.get(1).previousNode().value().value()));
    }

    @ParameterizedTest
    @MethodSource("org.finder.path.DataProvider#getPathCharactersAndNodes")
    public void testPathFilterOppositeNode(final List<PathCharacter> pathChars, final List<PathNode> node) {
        final List<PathCharacter> filteredChars = PathUtil.filterOppositeNode(pathChars, node.get(1));
        pathChars.removeAll(filteredChars);
        Assertions.assertTrue(filteredChars.size() != pathChars.size());
        Assertions.assertEquals(filteredChars.get(BEFORE_CHAR).value(), node.get(BEFORE_NODE).value().value());
        Assertions.assertEquals(filteredChars.get(AFTER_CHAR).value(), node.get(AFTER_NODE).value().value());
    }

    /**
     * Basic test.
     *
     * @param testMovesFile Path to test file {@link String}
     */
    @ParameterizedTest
    @CsvSource({
            "src//test//resources//move.txt",
    })
    public void testPathLook(final String testMovesFile) {
        final ConsoleLogger logger = new ConsoleLogger();
        final FilePathResolver resolver = new FilePathResolver(new HashMap<>(), logger);
        final List<PathCharacter> input = resolver.parseInput(testMovesFile);
        final LinkedHashMap<PathCharacter, PathNode> charNodeMap = new LinkedHashMap<>();
        for (PathCharacter character : input) {
            final PathNode lastNode = !charNodeMap.isEmpty() ?  new ArrayList<>(charNodeMap.values()).get(charNodeMap.size() - 1) : null;
            charNodeMap.put(character, new PathNodeFactory().create(character, character.index(), charNodeMap.size() > 0 ? lastNode : null));
        }

        final Finder<PathNode, PathCharacter> path = new Path(input, logger);
        final PathCharacter right = path.lookRight(charNodeMap.entrySet().stream().filter(pChar -> pChar.getKey().characterConfig().equals(DirectionConfig.START)).findFirst().get().getValue());
        final PathCharacter left = path.lookLeft(charNodeMap.entrySet().stream().filter(pChar -> pChar.getKey().characterConfig().equals(DirectionConfig.LEFT_OR_RIGHT)).findFirst().get().getValue());
        final PathCharacter up = path.lookUp(charNodeMap.entrySet().stream().filter(pChar -> pChar.getKey().characterConfig().equals(DirectionConfig.START)).findFirst().get().getValue());
        final PathCharacter down = path.lookDown(charNodeMap.entrySet().stream().filter(pChar -> pChar.getKey().characterConfig().equals(DirectionConfig.START)).findFirst().get().getValue());

        Assertions.assertEquals(right.value(), charNodeMap.get(right).value().value());
        Assertions.assertEquals(left.value(), charNodeMap.get(left).value().value());
        Assertions.assertEquals(up.value(), charNodeMap.get(up).value().value());
        Assertions.assertEquals(down.value(), charNodeMap.get(down).value().value());
    }
}
