package org.finder.path.core.factory.impl;

import org.finder.path.core.DirectionConfig;
import org.finder.path.core.factory.PathFactory;
import org.finder.path.core.model.PathCharacter;

import static org.finder.path.core.Constants.CHARACTER;
import static org.finder.path.core.Constants.CHARACTER_INDEX;

public class PathCharacterFactory extends PathFactory<PathCharacter> {

    @Override
    protected PathCharacter createInstance(final Object... argumenst) {
        return new PathCharacter(
                (Integer) argumenst[CHARACTER_INDEX],
                (DirectionConfig)DirectionConfig.getDirectionConfig().values().stream()
                        .filter(directionConfig -> {
                            if (directionConfig instanceof DirectionConfig) {
                                return ((DirectionConfig) directionConfig).matches(Character.toString((Character) argumenst[CHARACTER]));
                            }
                            return false;
                        }).findFirst().get(),
                Character.toString((Character) argumenst[CHARACTER])
        );
    }
}
