package org.testcontainers.testing.testng;

import org.testcontainers.containercore.Container;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * TODO: Javadocs
 */
public abstract class TestcontainersBaseTest {

    @BeforeClass
    public void startContainers() {
        applyToAllContainers(Container::start);
    }

    @AfterClass
    public void stopContainers() {
        applyToAllContainers(Container::stop);
    }

    private void applyToAllContainers(Consumer<Container> consumer) {
        // super simplistic and illustrative only. Is this way of doing things even idiomatic TestNG?
        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                if (field.getType().isAssignableFrom(Container.class)) {
                    field.setAccessible(true);
                    final Container container = (Container) field.get(this);
                    consumer.accept(container);
                }
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
