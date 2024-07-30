package org.finder.path.core;

import org.finder.path.core.model.PathCharacter;
import org.finder.path.core.model.PathNode;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.finder.path.core.DirectionConfig.NONE;

public final class PathUtil {

    private PathUtil() { }

    public static List<PathCharacter> filterOutPreviousNode(final List<PathCharacter> possiblePaths, final PathNode currentNode) {
        return possiblePaths
                 .stream()
                .filter(node -> currentNode.previousNode() != null &&
                        !Objects.equals(node.index(), currentNode.previousNode().currentIndex()))
                .toList();

    }

    public static List<PathCharacter> filterOppositeNode(final List<PathCharacter> possiblePaths, final PathNode currentNode) {
        return possiblePaths.stream()
                .filter(node -> Objects.equals(Math.subtractExact(Math.max(currentNode.previousNode().currentIndex(), currentNode.currentIndex()),
                                Math.min(currentNode.previousNode().currentIndex(), currentNode.currentIndex())),
                        Math.subtractExact(Math.max(currentNode.currentIndex(), node.index()),
                                Math.min(currentNode.currentIndex(), node.index()))
                ))
                .toList();
    }

    public static PathCharacter getPathCharacterAt(final List<PathCharacter> pathMap, final int index) {
        final Optional<PathCharacter> character = pathMap.stream()
                .filter(pathCharacter -> Objects.equals(pathCharacter.index(), index) &&
                        !Objects.equals(pathMap.size(), index))
                .findFirst();

        if (character.isPresent()) {
            return character.get();
        }

        return new PathCharacter(index, NONE, "");
    }

    public static String getPathValue(final Map<DirectionConfig, PathCharacter> value, final PathNode previousNode) {
        return previousNode != null ?
                previousNode.currentPath().concat(value.entrySet().stream().findFirst().get().getValue().value()) :
                value.entrySet().stream().findFirst().get().getValue().value();
    }

    public static String getLetterValue(final Map<DirectionConfig, PathCharacter> value, final PathNode previousNode) {
        if (value.isEmpty()) {
            return "";
        }

        final boolean isNodeVisited = !value.entrySet().stream().findFirst().get().getValue().isVisited();
        return  visitedCharacterIsLetterAndNotStartOrEnd(isNodeVisited, value, previousNode) ?
                previousNode.letters().concat(value.entrySet().stream().findFirst().get().getValue().value()) :
                previousNode != null ? previousNode.letters() : "";
    }

    private static boolean visitedCharacterIsLetterAndNotStartOrEnd(final boolean isNodeVisited, final Map<DirectionConfig, PathCharacter> value, final PathNode previousNode) {
        final boolean notStartOrEnd = value.get(DirectionConfig.END) == null && value.get(DirectionConfig.NONE) == null;
        return previousNode != null &&
                (notStartOrEnd && isNodeVisited && Character.isLetter(value.entrySet().stream().findFirst().get().getValue().value().toCharArray()[0]));
    }
}
