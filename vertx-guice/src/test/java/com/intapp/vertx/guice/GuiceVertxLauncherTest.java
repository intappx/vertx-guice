package com.intapp.vertx.guice;

import com.intapp.vertx.guice.stubs.VerticleWithVertxDependency;

import io.vertx.test.core.VertxTestBase;

import org.junit.Before;
import org.junit.Test;

/**
 * Implements test to verify work of the {@link GuiceVertxLauncher} class.
 */
public class GuiceVertxLauncherTest extends VertxTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        VerticleWithVertxDependency.instanceCount.set(0);
    }

    /**
     * Verifies that verticle with Vertx instance dependency in constructor can be deployed and run successfully.
     */
    @Test
    public void testRun_VerticleWithDependency_VerticleRunSuccessfully() throws Exception {
        // Arrange
        String[] args =
            {"run", GuiceVerticleFactory.PREFIX + ":" + VerticleWithVertxDependency.class.getCanonicalName()};

        GuiceVertxLauncher launcher = new GuiceVertxLauncher();

        // Act
        launcher.dispatch(args);
        waitUntil(() -> VerticleWithVertxDependency.instanceCount.get() == 1);

        // Assert
        org.assertj.core.api.Assertions.assertThat(
            VerticleWithVertxDependency.instanceCount.get()).isGreaterThan(0);
    }
}
