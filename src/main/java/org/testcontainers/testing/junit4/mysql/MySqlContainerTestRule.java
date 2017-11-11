package org.testcontainers.testing.junit4.mysql;

import lombok.experimental.Delegate;
import org.testcontainers.containercore.BaseContainerBuilder;
import org.testcontainers.containercore.Container;
import org.testcontainers.containercore.Plugin;
import org.testcontainers.testing.junit4.TestRuleImplementation;

/**
 * TODO: Javadocs
 */
public class MySqlContainerTestRule implements TestRuleImplementation {

    @Delegate
    private final Container container;

    private MySqlContainerTestRule(Container container) {
        this.container = container;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    public static MySqlContainerTestRule.Builder builder() {
        return new MySqlContainerTestRule.Builder()
                .withPlugin(new MySqlContainerPlugin());
    }

    public String getUsername() {
        return "some value";
    }

    public static class Builder extends BaseContainerBuilder<MySqlContainerTestRule.Builder> {
        private Builder() {
            super();
        }

        public MySqlContainerTestRule build() {
            validate();
            return new MySqlContainerTestRule(container);
        }
    }
}

class MySqlContainerPlugin implements Plugin {

}