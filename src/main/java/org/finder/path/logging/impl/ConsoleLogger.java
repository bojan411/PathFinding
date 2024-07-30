package org.finder.path.logging.impl;

import org.finder.path.logging.Logger;

@SuppressWarnings("checkstyle:Regexp")
public class ConsoleLogger implements Logger {
    @Override
    public void logError(final String message) {
        System.out.println("[ERROR] " + message);
    }

    @Override
    public void logInfo(final String message) {
        System.out.println("[INFO] " + message);
    }

    @Override
    public void logWarning(final String message) {
        System.out.println("[WARNING] " + message);
    }
}
