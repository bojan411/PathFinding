package org.finder.path.core.model;

import org.finder.path.core.DirectionConfig;
import org.finder.path.core.exceptions.PathValidator;
import org.finder.path.core.exceptions.ValidationError;

import java.util.Map;

import static org.finder.path.core.PathUtil.getPathValue;
import static org.finder.path.core.PathUtil.getLetterValue;

public record PathNode(PathCharacter value,
                       int currentIndex,
                       PathNode previousNode,
                       String currentPath,
                       String letters) implements PathValidator<PathNode> {

    public PathNode(final Map<DirectionConfig, PathCharacter> value, final int currentIndex, final PathNode previousNode) {
        this(value.entrySet().stream().findFirst().get().getValue(),
                currentIndex,
                previousNode,
                getPathValue(value, previousNode),
                getLetterValue(value, previousNode)
        );
        this.value().setVisited(false);
    }

    @Override
    public PathNode validate() throws ValidationError {
        return this;
    }
}
