package org.testcontainers.testing.junit4.mysql;

import org.testcontainers.containercore.BaseContainerBuilder;

/**
 * TODO: Javadocs
 */
public class MySqlContainerTestRuleBuilder extends BaseContainerBuilder<MySqlContainerTestRuleBuilder> {
    MySqlContainerTestRuleBuilder() {
        super();
    }

    public MySqlContainerTestRule build() {
        validate();
        return new MySqlContainerTestRule(container);
    }
}
