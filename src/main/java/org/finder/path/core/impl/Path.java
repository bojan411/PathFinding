package org.finder.path.core.impl;

import org.finder.path.Direction;
import org.finder.path.core.DirectionConfig;
import org.finder.path.core.Finder;
import org.finder.path.core.PathUtil;
import org.finder.path.core.exceptions.PathValidator;
import org.finder.path.core.exceptions.ValidationError;
import org.finder.path.core.exceptions.validation.BrokenPathError;
import org.finder.path.core.exceptions.validation.FakeTurnError;
import org.finder.path.core.exceptions.validation.ForkDetectedError;
import org.finder.path.core.exceptions.validation.MultipleEndingPointError;
import org.finder.path.core.exceptions.validation.MultiplePossibleDirectionsError;
import org.finder.path.core.exceptions.validation.MultipleStartingPointError;
import org.finder.path.core.exceptions.validation.MultipleStartingPathsError;
import org.finder.path.core.exceptions.validation.NoEndingPointError;
import org.finder.path.core.exceptions.validation.NoStartingPointError;
import org.finder.path.core.model.PathCharacter;
import org.finder.path.core.model.PathNode;
import org.finder.path.core.model.ResolvedPath;
import org.finder.path.logging.Logger;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.finder.path.core.Constants.ONE_POSSIBLE_PATH;
import static org.finder.path.core.Constants.TWO_POSSIBLE_PATHS;
import static org.finder.path.core.Constants.THREE_POSSIBLE_PATHS;
import static org.finder.path.core.Constants.ONE_END;
import static org.finder.path.core.Constants.TWO_ENDS;
import static org.finder.path.core.Constants.TWO_OF_SAME_VALUE;
import static org.finder.path.core.DirectionConfig.START;
import static org.finder.path.core.DirectionConfig.END;
import static org.finder.path.core.DirectionConfig.NONE;
import static org.finder.path.core.DirectionConfig.INTERSECTION;
import static org.finder.path.core.DirectionConfig.INTERSECTION_SPECIAL_CHAR;
import static org.finder.path.Direction.ITEMS_PER_GROUP;


public class Path implements PathValidator<Path>, Finder<PathNode, PathCharacter> {
    private final Logger log;
    private final PathNode startNode;
    private final List<PathNode> endNode;
    private final List<PathCharacter> pathMap;
    private final int itemsPerGroup;

    public Path(final List<PathCharacter> inputMap, final Logger log) {
        this.log = log;
        this.pathMap = inputMap;
        this.startNode = get(START).getFirst();
        this.endNode = get(END);
        this.itemsPerGroup = (Integer) DirectionConfig.valueOfDirectionConfig(ITEMS_PER_GROUP);
    }

    public ResolvedPath move() {
        this.log.logInfo("Processing map...");
        final PathNode lastNode = traversePath(this.startNode);
        this.log.logInfo("Finished processing map.");
        return new ResolvedPath(lastNode.currentPath(), lastNode.letters());

    }

    private PathNode traversePath(final PathNode currentStep) {
        this.log.logInfo(
                String.format("Processing node %s with index %d.",
                        currentStep.value().value(), currentStep.currentIndex()));
        if (END.equals(currentStep.value().characterConfig())) {
            return  currentStep;
        }

        final PathNode nextStep = determineNextStep(currentStep);
        this.log.logInfo(
                String.format("Next node is %s with index %d.",
                        nextStep.value().value(), nextStep.currentIndex()));
        return traversePath(nextStep);
    }

        public List<PathNode> get(final DirectionConfig conf) {
        final List<PathNode> node = new ArrayList<>();
        for (PathCharacter character : this.pathMap) {
            if (conf.equals(character.characterConfig())) {
                final PathNode pathNode = new PathNode(Collections.singletonMap(conf, character), character.index(), null).validate();
                if (pathNode.value().characterConfig().equals(START)) {
                    pathNode.value().setVisited(true);
                }
                node.add(pathNode);
            }
        }

        if (conf.equals(START) && node.isEmpty()) {
            throw new NoStartingPointError("No starting value found. Starting value configured: " + START.asString());
        }
        return node;
    }

    public PathNode determineNextStep(final PathNode currentStep) {
        final Direction direction;
        direction = DirectionConfig.valueOfDirection(currentStep.value().value());

        PathCharacter nextStep = currentStep.value();
        switch (direction) {
            case NONE:
                throw new BrokenPathError("Path is broken.");
            case START:
            case INTERSECTION:
            case INTERSECTION_SPECIAL_CHAR:
                nextStep = lookAround(currentStep).getFirst();
                break;
            case UP_OR_DOWN:
                final boolean isLastStepBehind = currentStep.previousNode().currentIndex() < currentStep.currentIndex();
                nextStep = isLastStepBehind ? lookDown(currentStep) : lookUp(currentStep);
                break;
            case LEFT_OR_RIGHT:
                final int moveTo = Math.subtractExact(currentStep.currentIndex(), currentStep.previousNode().currentIndex());
                if (Math.abs(moveTo) == 1) {
                    nextStep = moveTo > 0 ? lookRight(currentStep) : lookLeft(currentStep);
                } else {
                    nextStep = lookAround(currentStep).getFirst();
                }
                break;
            default:
        }
        currentStep.value().setVisited(true);
        return new PathNode(Collections.singletonMap(nextStep.characterConfig(), nextStep), nextStep.index(), currentStep).validate();
    }

    public List<PathCharacter> lookAround(final PathNode currentNode) {
        final List<PathCharacter> stepLookup = new ArrayList<>();
        stepLookup.add(lookRight(currentNode));
        stepLookup.add(lookLeft(currentNode));
        stepLookup.add(lookDown(currentNode));
        stepLookup.add(lookUp(currentNode));

        List<PathCharacter> newStepLookup = stepLookup
                .stream()
                .filter(node -> !NONE.equals(node.characterConfig()))
                .filter(node -> currentNode.previousNode() == null || !Objects.equals(node.index(), currentNode.previousNode().currentIndex()))
                .toList();

        if (newStepLookup.size() == TWO_POSSIBLE_PATHS && (INTERSECTION_SPECIAL_CHAR.equals(currentNode.value().characterConfig()) || INTERSECTION.equals(currentNode.value().characterConfig()))) {
            this.log.logInfo("Two possible paths, narrowing...");

            final List<PathCharacter> opposite = PathUtil.filterOppositeNode(newStepLookup, currentNode);

            if (opposite.size() == ONE_POSSIBLE_PATH &&
                    !Objects.equals(currentNode.previousNode().value(), opposite.getFirst().value())) {
                newStepLookup = newStepLookup.stream()
                        .filter(node -> !Objects.equals(node.value(), opposite.getFirst().value()))
                        .toList(); //visited ovdje koristiti da pojednostavim????
            }
        }

        if (newStepLookup.size() == THREE_POSSIBLE_PATHS) {
            this.log.logInfo("Three possible paths, narrowing...");
            newStepLookup = PathUtil.filterOutPreviousNode(newStepLookup, currentNode);

            newStepLookup = newStepLookup.stream()
                    .filter(node -> !node.isVisited())
                    .toList();

            if (newStepLookup.size() == THREE_POSSIBLE_PATHS) {
                newStepLookup = PathUtil.filterOppositeNode(newStepLookup, currentNode);
            }
        }
        return newStepLookup;
    }

    public PathCharacter lookRight(final PathNode currentNode) {
        this.log.logInfo("Looking right...");
        final Integer newIndex = currentNode.currentIndex() + 1;
        if (newIndex % this.itemsPerGroup < this.itemsPerGroup) {
            return PathUtil.getPathCharacterAt(pathMap, newIndex);
        }
        return new PathCharacter(newIndex, NONE, "");
    }

    public PathCharacter lookLeft(final PathNode currentNode) {
        this.log.logInfo("Looking left...");
        final Integer newIndex = currentNode.currentIndex() - 1;
        if (newIndex % this.itemsPerGroup < this.itemsPerGroup &&
                currentNode.currentIndex() % this.itemsPerGroup != 0) {
            return PathUtil.getPathCharacterAt(pathMap, newIndex);
        }
        return new PathCharacter(newIndex, NONE, "");
    }

    public PathCharacter lookDown(final PathNode currentNode) {
        this.log.logInfo("Looking down...");
        final Integer newIndex = currentNode.currentIndex() + this.itemsPerGroup;
        if (currentNode.currentIndex() + this.itemsPerGroup < pathMap.size()) {
            return PathUtil.getPathCharacterAt(pathMap, newIndex);
        }
        return new PathCharacter(newIndex, NONE, "");
    }

    public PathCharacter lookUp(final PathNode currentNode) {
        this.log.logInfo("Looking up...");
        final Integer newIndex = currentNode.currentIndex() - this.itemsPerGroup;
        if (currentNode.currentIndex() - this.itemsPerGroup >= 0) {
            return PathUtil.getPathCharacterAt(pathMap, newIndex);
        }
        return new PathCharacter(newIndex, NONE, "");
    }

    @Override
    public Path validate() throws ValidationError {
        this.log.logInfo("Validation started...");
        final List<Boolean> startingNode = new ArrayList<>();
        final List<Boolean> endingNode = new ArrayList<>();

        this.pathMap.forEach(pathCharacter -> {

            if (pathCharacter.characterConfig().equals(END)) {
                endingNode.add(true);
            }
            if (pathCharacter.characterConfig().equals(START)) {
                startingNode.add(true);
            }
        });

        if (startingNode.size() > 1) {
            throw new MultipleStartingPointError("Multiple starting values found.");
        }

        final List<PathCharacter> nodes = lookAround(get(START).getFirst());
        if (nodes.size() == TWO_POSSIBLE_PATHS) {
            throw new MultipleStartingPathsError("Multiple starting paths found.");
        }

        if (endingNode.isEmpty()) {
            throw new NoEndingPointError("No ending value found. End value configured: " + END.asString());
        }

        final List<PathNode> specialNode = get(INTERSECTION_SPECIAL_CHAR);
        specialNode.stream().forEach(node -> {
            final PathCharacter upChar = lookUp(node);
            final PathCharacter downChar = lookDown(node);
            final PathCharacter leftChar = lookLeft(node);
            final PathCharacter rightChar = lookRight(node);

            final List<PathCharacter> possiblePaths = Arrays.asList(upChar, downChar, leftChar, rightChar).stream()
                    .filter(character -> !NONE.equals(character.characterConfig()))
                    .toList();

            final HashMap<PathCharacter, Integer> indexDiff = new HashMap<>();
            for (PathCharacter ch : possiblePaths) {
                indexDiff.put(ch, Math.subtractExact(Math.max(node.currentIndex(), ch.index()), Math.min(node.currentIndex(), ch.index())));
            }

            final boolean hasOpposingNode = indexDiff.entrySet().stream()
                    .collect(groupingBy(Map.Entry::getValue))
                    .values().stream()
                    .filter(m -> m.size() > 1)
                    .flatMap(m -> m.stream())
                    .count() == TWO_OF_SAME_VALUE;

            final Set<String> valueAsString = indexDiff.entrySet().stream()
                    .collect(groupingBy(Map.Entry::getValue))
                    .values().stream()
                    .flatMap(m -> m.stream())
                    .map(p -> p.getKey().value())
                    .collect(Collectors.toSet());

            if (hasOpposingNode && valueAsString.size() == ONE_POSSIBLE_PATH && possiblePaths.size() == TWO_POSSIBLE_PATHS && endNode.size() == 1) {
                throw new FakeTurnError("Fake turn detected.");
            }

            if (valueAsString.size() == TWO_POSSIBLE_PATHS && possiblePaths.size() == THREE_POSSIBLE_PATHS && endNode.size() == TWO_ENDS) {
                throw new ForkDetectedError("Fork not allowed.");
            }

            if (valueAsString.size() == TWO_POSSIBLE_PATHS && possiblePaths.size() == THREE_POSSIBLE_PATHS && endNode.size() == ONE_END) {
                throw new MultiplePossibleDirectionsError("Multiple possible directions not allowed.");
            }
            if (endNode.size() > 1) {
                throw new MultipleEndingPointError("Multiple ending values found.");
            }
        });
        this.log.logInfo("Validation finished...");
        return this;
    }
}
