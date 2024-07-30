package org.finder.path;

import org.junit.jupiter.params.provider.Arguments;
import org.finder.path.core.DirectionConfig;
import org.finder.path.core.factory.impl.PathCharacterFactory;
import org.finder.path.core.factory.impl.PathNodeFactory;
import org.finder.path.core.model.PathCharacter;
import org.finder.path.core.model.PathNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class DataProvider {

    private DataProvider() { }

    public static Stream<Arguments> getArgumentData() {
        return Stream.of(
                Arguments.of("-f", "File", org.finder.path.args.Arguments.PATH_LOCATION),
                Arguments.of("-n", "None", org.finder.path.args.Arguments.NONE),
                Arguments.of("-e", "End", org.finder.path.args.Arguments.END),
                Arguments.of("-s", "Start", org.finder.path.args.Arguments.START),
                Arguments.of("-ud", "UpDown", org.finder.path.args.Arguments.UP_OR_DOWN),
                Arguments.of("-lr", "LeftRight", org.finder.path.args.Arguments.LEFT_OR_RIGHT),
                Arguments.of("-i", "Intersection", org.finder.path.args.Arguments.INTERSECTION),
                Arguments.of("-isc", "Intersection special char", org.finder.path.args.Arguments.INTERSECTION_SPECIAL_CHAR)
        );
    }

    public static Stream<Arguments> getHappyPathData() {
        return Stream.of(
                Arguments.of("src//test//resources//basic.txt", "ACB", "@---A---+|C|+---+|+-B-x"),
                Arguments.of("src//test//resources//intersections.txt", "ABCD", "@|A+---B--+|+--C-+|-||+---D--+|x"),
                Arguments.of("src//test//resources//lettersOnTurns.txt", "ACB", "@---A---+|||C---+|+-B-x"),
                Arguments.of("src//test//resources//letterSameLocation.txt", "GOONIES", "@-G-O-+|+-+|O||+-O-N-+|I|+-+|+-I-+|ES|x"),
                Arguments.of("src//test//resources//keepDirection.txt", "BLAH", "@B+++B|+-L-+A+++A-+Hx")
                );
    }

        public static Stream<Arguments> getErrorPathData() {
            return Stream.of(
                    Arguments.of("src//test//resources//noStart.txt"),
                    Arguments.of("src//test//resources//noEnd.txt"),
                    Arguments.of("src//test//resources//multipleStarts.txt"),
                    Arguments.of("src//test//resources//multipleEnds.txt"),
                    Arguments.of("src//test//resources//fork.txt"),
                    Arguments.of("src//test//resources//brokenPath.txt"),
                    Arguments.of("src//test//resources//multipleStartingPaths.txt"),
                    Arguments.of("src//test//resources//fakeTurn.txt"),
                    Arguments.of("src//test//resources//twoDirections.txt")
            );
        }

        public static Stream<Arguments> getDirectionConfigData() {
            return Stream.of(
                    Arguments.of(Direction.UP_OR_DOWN, "#"),
                    Arguments.of(Direction.LEFT_OR_RIGHT, "-"),
                    Arguments.of(Direction.INTERSECTION, "Y"),
                    Arguments.of(Direction.INTERSECTION_SPECIAL_CHAR, "Q")
            );
        }

    public static Stream<Arguments> getDuplicateDirectionConfigData() {
        return Stream.of(
                Arguments.of(Direction.NONE, "x")
        );
    }

    public static Stream<Arguments> getPathCharactersAndNodes() {
        final HashMap<Direction, String> config =  new HashMap<>();
        config.put(Direction.NONE, " ");
        config.put(Direction.UP_OR_DOWN, "#");
        config.put(Direction.LEFT_OR_RIGHT, "-");
        config.put(Direction.INTERSECTION, "Y");
        config.put(Direction.INTERSECTION_SPECIAL_CHAR, "Q");

        DirectionConfig.configure(config);
        DirectionConfig.getDirectionConfig().remove(Direction.ITEMS_PER_GROUP);

        final List<PathCharacter> possiblePaths = generateBatchCharInstances();
        final List<PathNode> nodes = generateBatchNodeInstances(possiblePaths);

        return Stream.of(Arguments.of(possiblePaths, nodes));
    }

    private static List<PathCharacter> generateBatchCharInstances() {
        final List<PathCharacter> possiblePaths = new ArrayList<>();

        IntStream.range(0, DirectionConfig.getDirectionConfig().values().size())
                .forEach(idx -> {
                    final Object val = DirectionConfig.getDirectionConfig().values().stream().toList().get(idx);
                    if (val instanceof DirectionConfig) {
                        possiblePaths.add(
                                new PathCharacterFactory().create(((DirectionConfig)val).asString().charAt(0), idx));
                    }
                });

        return possiblePaths;
    }

    private static List<PathNode> generateBatchNodeInstances(final List<PathCharacter> possiblePaths) {
        final List<PathNode> nodes = new ArrayList<>();
        final PathNodeFactory pathNodeFactory = new PathNodeFactory();

        IntStream.range(0, DirectionConfig.getDirectionConfig().values().size())
                .forEach(idx -> {
                    final Object val = DirectionConfig.getDirectionConfig().values().stream().toList().get(idx);
                    if (val instanceof DirectionConfig) {
                        nodes.add(
                                pathNodeFactory.create(
                                        possiblePaths.stream().filter(pChar -> pChar.characterConfig().equals(val)).findFirst().get(),
                                        idx,
                                        nodes.size() > 0 ? nodes.get(idx - 1) : null)
                        );
                    }
                });

        return nodes;
    }
}
