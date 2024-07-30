package org.finder.path.service.impl;

import org.finder.path.core.model.ResolvedPath;
import org.finder.path.core.PathResolver;
import org.finder.path.core.exceptions.ValidationError;
import org.finder.path.service.Processor;

public class PathProcessor implements Processor<ResolvedPath> {
    private final PathResolver pathResolver;

    public PathProcessor(final PathResolver pathResolver) {
        this.pathResolver = pathResolver;
    }

    public ResolvedPath process(final Object map) {
        try {
            return this.pathResolver.resolve(map);
        } catch (ValidationError e) {
            this.pathResolver.getLogger().logError(e.getMessage());
            for (String line : this.pathResolver.getParsedMap()) {
                this.pathResolver.getLogger().logError(line);
            }
            return null;
        }
    }
}
