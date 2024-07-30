package org.finder.path.core.model;

import org.finder.path.core.DirectionConfig;
import org.finder.path.core.exceptions.PathValidator;
import org.finder.path.core.exceptions.validation.UnsupportedCharacterError;
import org.finder.path.core.exceptions.ValidationError;

public final class PathCharacter implements PathValidator<PathCharacter> {

    private final Integer index;
    private final DirectionConfig characterConfig;
    private final String value;
    private boolean isVisited;

    public PathCharacter(final Integer index, final DirectionConfig characterConfig, final String value) {
        this.index = index;
        this.characterConfig = characterConfig;
        this.value = value;
        this.isVisited = false;
    }

    public Integer index() {
        return index;
    }

    public String value() {
        return value;
    }

    public DirectionConfig characterConfig() {
        return characterConfig;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(final boolean visited) {
        isVisited = visited;
    }

    @Override
    public PathCharacter validate() throws ValidationError {
        DirectionConfig.getDirectionConfig().values().stream()
                .filter(entry -> {
                    if (entry instanceof DirectionConfig) {
                        return entry.equals(this.characterConfig());
                    }
                    return false;
                })
                .findFirst()
                .orElseThrow(() -> new UnsupportedCharacterError("Character not supported: " + this.value));

        return this;
    }
}
