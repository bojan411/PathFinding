package org.finder.path.core.factory.impl;

import org.finder.path.core.factory.PathFactory;
import org.finder.path.core.model.PathCharacter;
import org.finder.path.core.model.PathNode;

import static org.finder.path.core.Constants.PATH_CHARACTER;
import static org.finder.path.core.Constants.INDEX;
import static org.finder.path.core.Constants.PATH_NODE;

public class PathNodeFactory extends PathFactory<PathNode> {

    @Override
    protected PathNode createInstance(final Object... argumenst) {
        final PathCharacter pathChar = (PathCharacter) argumenst[PATH_CHARACTER];
        final Integer index = (Integer) argumenst[INDEX];
        final PathNode prevNode = argumenst[PATH_NODE] != null ? (PathNode) argumenst[PATH_NODE] : null;
        return new PathNode(pathChar, index, prevNode, "", "");
    }
}
