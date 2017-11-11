package org.testcontainers.testing.junit4;

import lombok.experimental.Delegate;
import org.testcontainers.containercore.Container;

/**
 * TODO: Javadocs
 */
public class ContainerTestRule implements TestRuleImplementation {

    @Delegate
    private final Container container;

    ContainerTestRule(Container container) {
        this.container = container;
    }

    @Override
    public Container getContainer() { // this method exists so that `TestRuleImplementation`
                                      //  can access the container
        return container;
    }

    public static ContainerTestRuleBuilder builder() {
        return new ContainerTestRuleBuilder();
    }

}
