package org.finder.path.adapter;

import org.finder.path.Direction;
import org.finder.path.core.DirectionConfig;
import org.finder.path.core.model.PathCharacter;
import org.finder.path.core.PathResolver;
import org.finder.path.core.factory.impl.PathCharacterFactory;
import org.finder.path.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FilePathResolver extends PathResolver {
    public FilePathResolver(final HashMap<Direction, String> config, final Logger logger) {
        super(config, logger);
    }

    @Override
    public List<PathCharacter> parseInput(final Object inputMap) {
        this.getLogger().logInfo("Parsing input...");
        if (inputMap == null) {
            return null;
        }

        final Path file = Paths.get((String)inputMap);
        final List<PathCharacter> resultMap = new ArrayList<>();
        try (Stream<String> stream = Files.lines(file)) {
            int index = 0;
            final List<String> lines = stream.toList();
            final int maxLineSize = lines.stream().max(Comparator.comparingInt(String::length)).get().length();
            this.setConfig(Direction.ITEMS_PER_GROUP, maxLineSize);

            for (String line : lines) {
                String newLine = line;
                if (line.length() < maxLineSize) {
                    final String noneValue = this.getValueConfiguredFor(lines, Direction.NONE);
                    newLine = line + noneValue.repeat(maxLineSize - line.length());
                }

                for (char character : newLine.toCharArray()) {
                    resultMap.add(new PathCharacterFactory().create(character, index));
                    index += 1;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return resultMap;
    }

    private String getValueConfiguredFor(final List<String> lines, final Direction direction) {
        final Optional<Character> value = lines.stream()
                .flatMap(ln -> ln.chars().mapToObj(c -> (char) c))
                .filter( character -> ((DirectionConfig)getConfig()
                        .entrySet()
                        .stream()
                        .filter(conf -> conf.getKey().equals(direction))
                        .findFirst()
                        .get()
                        .getValue())
                        .matches(Character.toString(character)))
                .findFirst();

        return value.map(Object::toString).orElse(" ");
    }
}
