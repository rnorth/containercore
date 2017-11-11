package org.testcontainers.containercore;

/**
 * TODO: Javadocs
 */
public abstract class BaseContainerBuilder<T extends BaseContainerBuilder> {
    protected final Container container;

    @SuppressWarnings("unchecked")
    private T self = (T) this;

    protected BaseContainerBuilder() {
        this.container = new Container();
    }

    public T withImage(String image) {
        container.image = image;
        return self;
    }

    public T withExposedPort(int port) {
        container.exposedPorts.add(port);
        return self;
    }

    public T withPlugin(Plugin plugin) {
        this.container.plugins.add(plugin);
        return self;
    }

    protected void validate() {
        if (this.container.image == null) throw new NullPointerException("Image must be set");
    }
}
