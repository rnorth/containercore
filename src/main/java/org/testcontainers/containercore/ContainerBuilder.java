package org.testcontainers.containercore;

/**
 * TODO: Javadocs
 */
public class ContainerBuilder extends BaseContainerBuilder<ContainerBuilder> {
    public ContainerBuilder() {
        super();
    }

    public Container build() {
        validate();
        return container;
    }
}
