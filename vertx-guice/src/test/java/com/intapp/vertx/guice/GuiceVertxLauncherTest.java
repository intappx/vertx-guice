package com.intapp.vertx.guice;

import com.intapp.vertx.guice.stubs.VerticleWithVertxDependency;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

/**
 * Implements test to verify work of the {@link GuiceVertxLauncher} class.
 */
@ExtendWith(VertxExtension.class)
public class GuiceVertxLauncherTest {

    @BeforeEach
    public void setUp(Vertx vertx) throws Exception {
        VerticleWithVertxDependency.instanceCount.set(0);
    }

    /**
     * Verifies that verticle with Vertx instance dependency in constructor can be deployed and run successfully.
     */
    @Test
    public void testRun_VerticleWithDependency_VerticleRunSuccessfully(Vertx vertx) throws Exception {
        // Arrange
        String[] args =
            {"run", GuiceVerticleFactory.PREFIX + ":" + VerticleWithVertxDependency.class.getCanonicalName()};

        GuiceVertxLauncher launcher = new GuiceVertxLauncher();

        // Act
        launcher.dispatch(args);
        await().atMost(Duration.ofSeconds(5)).until(() -> VerticleWithVertxDependency.instanceCount.get() == 1);

        // Assert
        org.assertj.core.api.Assertions.assertThat(
            VerticleWithVertxDependency.instanceCount.get()).isGreaterThan(0);
    }
}
