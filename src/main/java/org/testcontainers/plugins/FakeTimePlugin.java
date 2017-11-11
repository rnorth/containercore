package org.testcontainers.plugins;

import org.testcontainers.containercore.Plugin;

/**
 * This plugin isn't real and doesn't work, but if it did, it would use faketime to
 * force the clock within the container to appear to report a specific date
 */
public class FakeTimePlugin implements Plugin {
    public FakeTimePlugin(String s) {
        super();
    }
}
