package org.finder.path;

import org.finder.path.adapter.FilePathResolver;
import org.finder.path.core.model.ResolvedPath;
import org.finder.path.args.ArgsUtil;
import org.finder.path.args.Arguments;
import org.finder.path.core.PathResolver;
import org.finder.path.logging.impl.ConsoleLogger;
import org.finder.path.logging.Logger;
import org.finder.path.service.impl.PathProcessor;

import java.util.HashMap;

public final class PathFinderApp {

    private PathFinderApp() { }

    @SuppressWarnings("checkstyle:Regexp")
    public static void main(final String[] args) {
        final HashMap<Arguments, HashMap<Direction, String>> arguments = ArgsUtil.parseArgs(args);
        if (arguments == null) {
            return;
        }

        final String fileLocation = ArgsUtil.getFileLocation(arguments);
        final HashMap<Direction, String> pathConfig = ArgsUtil.getPathConfig(arguments);

        final Logger log = new ConsoleLogger();
        final PathResolver pathResolver = new FilePathResolver(pathConfig, new ConsoleLogger());
        final PathProcessor processor = new PathProcessor(pathResolver);
        final ResolvedPath resolvedPath = processor.process(fileLocation);

        if (resolvedPath != null) {
            log.logInfo("#############################################################");
            log.logInfo("PATH AS STRING: " + resolvedPath.pathAsString());
            log.logInfo("LETTERS AS STRING: " + resolvedPath.letters());
            log.logInfo("#############################################################");
        }
    }
}