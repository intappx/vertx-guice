package com.ldclrcq.vertx.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ldclrcq.vertx.guice.stubs.VerticleWithVertxDependency;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rx.java.ObservableFuture;
import io.vertx.rx.java.RxHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Implements tests to verify logic of the {@see GuiceVertxDeploymentManager} class.
 */
@ExtendWith(VertxExtension.class)
public class GuiceVertxDeploymentManagerTest {
    @BeforeEach
    public void setUp(Vertx vertx) {
        Injector injector = Guice.createInjector(new VertxModule(vertx));

        GuiceVerticleFactory guiceVerticleFactory = new GuiceVerticleFactory(injector);
        vertx.registerVerticleFactory(guiceVerticleFactory);

        VerticleWithVertxDependency.instanceCount.set(0);
    }

    /**
     * Verifies that verticle with dependency can be deployed successfully.
     */
    @Test
    public void testDeployVerticle(Vertx vertx) {
        // Act
        GuiceVertxDeploymentManager deploymentManager = new GuiceVertxDeploymentManager(vertx);
        deploymentManager.deployVerticle(VerticleWithVertxDependency.class);
        await().atMost(Duration.ofSeconds(5)).until(() -> VerticleWithVertxDependency.instanceCount.get() == 1);

        // Assert
        assertThat(
                VerticleWithVertxDependency.instanceCount.get()).isGreaterThan(0);
    }

    /**
     * Verifies that verticle with dependency can be deployed successfully with specific deployment options.
     */
    @Test
    public void testDeployVerticleWithOptions(Vertx vertx) {
        // Act`
        GuiceVertxDeploymentManager deploymentManager = new GuiceVertxDeploymentManager(vertx);
        deploymentManager.deployVerticle(
                VerticleWithVertxDependency.class,
                new DeploymentOptions().setWorker(true));

        await().atMost(Duration.ofSeconds(5)).until(() -> VerticleWithVertxDependency.instanceCount.get() == 1);

        // Assert
        assertThat(
                VerticleWithVertxDependency.instanceCount.get()).isGreaterThan(0);
    }

    /**
     * Verifies that verticle with dependency can be deployed successfully and result of the deployment can be received
     * via providing completion handler.
     */
    @Test
    public void testDeployVerticleWithCompletionHandler(Vertx vertx) {
        // Act`
        ObservableFuture<String> deploymentResult = RxHelper.observableFuture();

        GuiceVertxDeploymentManager deploymentManager = new GuiceVertxDeploymentManager(vertx);
        deploymentManager.deployVerticle(
                VerticleWithVertxDependency.class,
                deploymentResult.toHandler());

        String deploymentId = deploymentResult.toBlocking().single();

        // Assert
        assertThat(deploymentId).isNotEmpty();
    }

    /**
     * Verifies that verticle with dependency can be deployed successfully and result of the deployment can be received
     * via providing completion handler.
     */
    @Test
    public void testDeployVerticleWithOptionsAndCompletionHandler(Vertx vertx) {
        // Act`
        ObservableFuture<String> deploymentResult = RxHelper.observableFuture();

        GuiceVertxDeploymentManager deploymentManager = new GuiceVertxDeploymentManager(vertx);
        deploymentManager.deployVerticle(
                VerticleWithVertxDependency.class,
                new DeploymentOptions(),
                deploymentResult.toHandler());

        String deploymentId = deploymentResult.toBlocking().single();

        // Assert
        assertThat(deploymentId).isNotEmpty();
    }

    @Test
    public void testMaybeDeployVerticleWithOptions(Vertx vertx, VertxTestContext context) {
        // ARRANGE
        GuiceVertxDeploymentManager deploymentManager = new GuiceVertxDeploymentManager(vertx);

        // ACT
        // ASSERT
        deploymentManager.maybeDeployVerticle(
                        VerticleWithVertxDependency.class,
                        new DeploymentOptions()
                ).onSuccess(deploymentId -> context.verify(() -> assertThat(deploymentId).isNotBlank())
                        .completeNow())
                .onFailure(context::failNow);
    }

    @Test
    public void testMaybeDeployVerticle(Vertx vertx, VertxTestContext context) {
        // ARRANGE
        GuiceVertxDeploymentManager deploymentManager = new GuiceVertxDeploymentManager(vertx);

        // ACT
        // ASSERT
        deploymentManager.maybeDeployVerticle(VerticleWithVertxDependency.class)
                .onSuccess(deploymentId -> context.verify(() -> assertThat(deploymentId).isNotBlank())
                        .completeNow())
                .onFailure(context::failNow);
    }

}