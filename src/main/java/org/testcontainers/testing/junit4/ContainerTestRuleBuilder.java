package org.testcontainers.testing.junit4;

import org.testcontainers.containercore.BaseContainerBuilder;

/**
 * TODO: Javadocs
 */
public class ContainerTestRuleBuilder extends BaseContainerBuilder<ContainerTestRuleBuilder> {
    ContainerTestRuleBuilder() {
        super();
    }

    public ContainerTestRule build() {
        validate();
        return new ContainerTestRule(container);
    }
}
