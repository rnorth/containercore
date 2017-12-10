import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.testing.junit4.ContainerTestRule;
import org.testcontainers.testing.junit4.mysql.MySqlContainerTestRule;

/**
 * TODO: Javadocs
 */
public class SimpleJUnit4Test {

    @Rule
    public ContainerTestRule alpine = ContainerTestRule.builder()
            .withImage("alpine:3.5")
            .withExposedPort(1234)
            .build();

    @Rule
    public MySqlContainerTestRule mysql = MySqlContainerTestRule.builder()
            .withImage("mysql:5.5")
            .withExposedPort(1234)
            .build();

    @Test
    public void doNothingTest() {
        alpine.exec("true");
    }

    @Test
    public void doSomethingWithMysqlTest() {
        final String username = mysql.getUsername();
    }
}
