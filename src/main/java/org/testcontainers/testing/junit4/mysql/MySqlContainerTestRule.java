package org.testcontainers.testing.junit4.mysql;

import lombok.experimental.Delegate;
import org.testcontainers.containercore.Container;
import org.testcontainers.containercore.Plugin;
import org.testcontainers.testing.junit4.TestRuleImplementation;

/**
 * TODO: Javadocs
 */
public class MySqlContainerTestRule implements TestRuleImplementation {

    @Delegate
    private final Container container;

    MySqlContainerTestRule(Container container) {
        this.container = container;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    public static MySqlContainerTestRuleBuilder builder() {
        return new MySqlContainerTestRuleBuilder()
                .withPlugin(new MySqlContainerPlugin());
    }

    public String getUsername() {
        return "some value";
    }

}

class MySqlContainerPlugin implements Plugin {

}