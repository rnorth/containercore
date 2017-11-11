package org.testcontainers.containercore;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Javadocs
 */
public class Container {

    // Example properties that would be used during startup/runtime
    List<Integer> exposedPorts = new ArrayList<>();
    String image;
    List<Plugin> plugins = new ArrayList<>();

    Container() {
        // package-scoped - instantiate using builder
    }

    // Generic container start action
    public void start() {
        System.out.println("Starting");
    }

    // Generic container stop action
    public void stop() {
        System.out.println("Stopping");
    }

    // An example of an action that can be performed on the container while running
    public void exec(String command) {
        System.out.println("Execing " + command);
    }

    public static ContainerBuilder builder() {
        return new ContainerBuilder();
    }

}
