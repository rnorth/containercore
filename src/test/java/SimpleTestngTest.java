import org.testcontainers.containercore.Container;
import org.testcontainers.testing.testng.TestcontainersBaseTest;
import org.testng.annotations.Test;

/**
 * TODO: Javadocs
 */
public class SimpleTestngTest extends TestcontainersBaseTest {

    private Container alpine = Container.builder()
            .withImage("alpine:3.5")
            .build();

    @Test
    public void doNothing() {
        alpine.exec("date");
    }
}
