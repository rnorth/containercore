package org.testcontainers.containercore;

/**
 * TODO: Javadocs
 */
public interface Plugin {

    // draft list of hook points

    default void beforeConfigure() {}
    default void afterConfigure() {}

    default void beforeStart() {}
    default void afterStart() {}

    default void waitForStarted() {}
    default void waitForReady() {}

    default void afterExit() {}
}
