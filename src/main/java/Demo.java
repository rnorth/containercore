import org.testcontainers.containercore.Container;
import org.testcontainers.plugins.FakeTimePlugin;

/**
 * TODO: Javadocs
 */
public class Demo {
    public static void main(String[] args) {

        // Building a simple container object not linked to any test framework
        final Container container = Container.builder()
                .withImage("alpine:3.5")
                .withExposedPort(1234)
                .build();

        // now that the container is built, the `with___` methods disappear and runtime methods,
        //  like `exec` become available
        container.start();
        container.exec("date");
        container.stop();


        // Using a plugin
        final Container containerWithPlugin = Container.builder()
                .withImage("alpine:3.5")
                .withPlugin(new FakeTimePlugin("2010-01-01 00:00:00"))
                .build();

        containerWithPlugin.start();
        containerWithPlugin.exec("date");
        containerWithPlugin.stop();
    }
}
