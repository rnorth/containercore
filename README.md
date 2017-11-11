# Proposal: Container Core

> A draft model for a test-framework-agnostic Docker Container abstraction (for Testcontainers 2.0)

Author: [Richard North](mailto:rich.north@gmail.com)

Date: October 2017



This repository contains a skeleton structure for a new library, Container Core, which is documented below as a proposal. 



## Goals

* **Continue to provide an object abstraction for Docker containers**: This seems to have proven to be a popular way to interace with Docker from Java
* **Allow Testcontainers to support test frameworks other than JUnit 4**: JUnit 5, Spock and TestNG would be the likely candidates
* **Expose a reasonably simple API that emphasises discoverability of features and ease of use**: Again, the fluent setter pattern has proven to work well
* **Allow us to clean up the Testcontainers API**: for example:
  * making sure all fluent setters are consistent
  * adding a separation between definition-time concerns and 'run-time' concerns
  * add a plugin model to allow *composition* to be used in addition to *inheritance*
* **Continue to support specialized container types via specialized classes** 
* **(Secondary)** Publish the library as a general purpose API for interacting with containers, and make Testcontainers into a test-oriented layer on top. There are valid non-test scenarios for using this library. The two libraries would become:
  * Container Core, a library focused on bridging the gaps between Docker and Java
  * Testcontainers 2.x, a library focused on using Container Core for testing
* **(Secondary, but important)** Even though there will be breaking API changes for Testcontainers 2.0, make best efforts to ensure a 1:1 mapping of concepts


---

##Runtime `Container` vs define-time `ContainerBuilder`

Separating out the API for 'actions that are available when a container is being defined' and 'actions that are available when the container' is running is important. It will help reduce the API surface area to only the methods that make sense in a given context. This should improve ease of use and discoverability of the API, as well as resulting in a cleaner codebase.

The `org.testcontainers.containercore` package shows the key classes for creating containers *that are not tied to any test framework*. Classes under this package would be **published as a ContainerCore JAR** (probably still under the org.testcontainers groupId, but a new artifact)

### End result / usage

The classes in this package allow for the following API and usage:

```java
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
```



### Implementation

* `Container` holds configuration data in fields, plus methods for 'run-time' interactions such as `start`, `stop` , `exec`, etc.
* `ContainerBuilder` (and its parent class `BaseContainerBuilder`)  exposes the define-time `withXYZ` fluent setter API
* `BaseContainerBuilder` uses package-scoped access to `Container`'s configuration fields to apply values before `build()` is called
* `ContainerBuilder` can also validate the container's configuration just prior to building, e.g. to ensure that essential fields like `image` are set




---

## Test-framework agnostic vs Test-framework coupled classes

The above example shows test-framework agnostic usage. Still to support use of JUnit 4 rules, we need an alternative Container implementation (implementing JUnit's `TestRule`) and an alternative Builder specialisation. 

### End result / usage (JUnit 4 case)

See the package `org.testcontainers.testing.junit4` - **this would be published as a Testcontainers JAR**.

The end result achieved is almost the same as the existing Testcontainers 1.x API. The only differences are usage of a different class name and use of the builder pattern.

```java
public class SimpleJUnit4Test {

    @Rule
    public ContainerTestRule alpine = ContainerTestRule.builder()
            .withImage("alpine:3.5")
            .withExposedPort(1234)
            .build();
  
    @Test
    public void doNothingTest() {
        alpine.exec("true");
    }

```



### Implementation

* `ContainerTestRule` should have a run-time API that matches that of `Container`. To achieve this with least fuss, we can use Lombok's `@Delegate`. In using Lombok, though, we should be certain to de-Lombok all published sources.
* `ContainerTestRuleBuilder` uses the common `BaseContainerBuilder` class so that the same fluent setter API is available when defining containers. A small but encapsulated amount of generics trickery is needed so that the fluent setter API returns the expected 'self' type.



### End result / usage (TestNG case; potentially others)

See `org.testcontainers.testing.testng`. *This is not mature, possibly not idiomatic, and only reflects a draft idea:*

```java
public class SimpleTestngTest extends TestcontainersBaseTest {

    private Container alpine = Container.builder()
            .withImage("alpine:3.5")
            .build();

    @Test
    public void doNothing() {
        alpine.exec("date");
    }
}
```

Note that:

* we use the plain `Container` class as a field in the test class
* The current (very rough) `TestcontainersBaseTest` class simulates rule-like behaviour: it iterates over fields via reflection and triggers start/stop at appropriate points in the test. This is likely to need a lot of refinement.
* It's possible that a similar approach might work for JUnit 5 in conjunction with `@ExtendWith`, but this needs proper investigation



---

## Specialised container classes

Some containers are greatly enhanced by addition of a handful of specialised methods; the database container types are one example.

### End result / usage

```java
public class SimpleJUnit4Test {

    @Rule
    public MySqlContainerTestRule mysql = MySqlContainerTestRule.builder()
            .withImage("mysql:5.5")
            .withExposedPort(1234)
            .build();

    @Test
    public void doSomethingWithMysqlTest() {
        final String username = mysql.getUsername();
    }
```



We can again accomplish a similar API to Testcontainers 1.x. The example above illustrates a getter for JDBC username on a MySql-specialized class.



### Implementation

Note that:

* To avoid the mental overhead of generics that current exists in Testcontainers 1.x, here the `MySqlContainerTestRule` class is *not* a subclass of `ContainerTestRule`. Instead, it inherits a default JUnit `apply` method from the `TestRuleImplementation` interface.
* As with `ContainerTestRule`, the `MySqlContainerTestRule` class _delegates_ to a `Container` instance using the Lombok `@Delegate` annotation.
* `MySqlContainerTestRule` does some further composition to customize the `Container` instance without resorting to subclassing - it binds a `Plugin` . In this example, the MySql-specific plugin class could do things such as setting environment variables and selecting a readiness check strategy. The next section descibes what a `Plugin` is!



---

## Plugins

Plugins are intended to allow extra behaviour to be added to `Container` via composition rather than inheritance. This should make it easier to share small, reusable, elements without tying them in to a particular branch of an inheritance hierarchy.

### End result / usage

This is draft and highly subject to change, but the Plugin API could look like the following.

For the developer, adding a plugin to a container would be as simple as calling the `withPlugin` method on the builder, e.g. for a fictitious plugin that adds FakeTime to containers:

```java
final Container containerWithPlugin = Container.builder()
  .withImage("alpine:3.5")
  .withPlugin(new FakeTimePlugin("2010-01-01 00:00:00"))
  .build();
```

A plugin would implement one or more of the methods on the `Plugin` interface (this is very incomplete, lacking parameters for example):

```java
public interface Plugin {

    // draft list of hook points

    default void beforeConfigure() {}
    default void afterConfigure() {}

    default void beforeStart() {}
    default void afterStart() {}

    default void waitForStarted() {}
    default void waitForReady() {}

    default void afterExit() {}
}
```

Plugins would be called in order at the appropriate points in a container's lifecycle.

Additionally it may be useful to allow the developer to write code that interacts with a plugin while containers are running. This could be debated, but perhaps the simplest model that could work is to have the test class hold a field reference to the plugin, e.g.:

```java
final FaketimePlugin fakeTime = new FakeTimePlugin("2010-01-01 00:00:00")

final Container containerWithPlugin = Container.builder()
  .withImage("alpine:3.5")
  .withPlugin(fakeTime)
  .build();

// later, while container is running
fakeTime.setTime("2011-01-01 00:00:00");
```



---

# Relationship between Testcontainers and Container Core

* Much of the 'guts' of Container Core would be taken out of the current Testcontainers 1.x
* It is important that Testcontainers 1.x continues to receive bug fixes for a good period of time. To this end, either:
  * Bug fixes could be backported between Container Core and Testcontainers 1.x
  * Or (perhaps later) Testcontainers 1.x could be adapted to use Container Core. However, we could only do this if we can avoid breaking API changes, and it's unlikely that Testcontainers 1.x could take advantage of all new features.
* Testcontainers 2.x would be developed on top of Container Core, taking full advantage of its API and features. As a smaller library, Testcontainers 2.x ought to be simpler and quicker to develop and test.
* Once Testcontainers 1.x and 2.x are both suitably stable, we would cease all but critical bug fixes to 1.x and recommend no new usage of the 1.x line.