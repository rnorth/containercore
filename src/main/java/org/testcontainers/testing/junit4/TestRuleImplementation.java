package org.testcontainers.testing.junit4;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;
import org.testcontainers.containercore.Container;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Javadocs
 */
public interface TestRuleImplementation extends TestRule {

    @Override
    default Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                List<Throwable> errors = new ArrayList<Throwable>();

                try {
                    getContainer().start();
                    base.evaluate();
                } catch (Throwable e) {
                    errors.add(e);
                } finally {
                    getContainer().stop();
                }

                MultipleFailureException.assertEmpty(errors);
            }
        };
    }

    Container getContainer();
}
