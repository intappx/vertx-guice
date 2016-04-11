package com.intapp.vertx.guice;

import com.intapp.vertx.guice.stubs.VerticleWithVertxDependency;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.vertx.core.DeploymentOptions;
import io.vertx.rx.java.ObservableFuture;
import io.vertx.rx.java.RxHelper;
import io.vertx.test.core.VertxTestBase;

import org.junit.Before;
import org.junit.Test;

/**
 * Implements tests to veriry logic of the {@see GuiceVertxDeploymentManager} class.
 */
public class GuiceVertxDeploymentManagerTest extends VertxTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        Injector injector = Guice.createInjector(new VertxModule(this.vertx));

        GuiceVerticleFactory guiceVerticleFactory = new GuiceVerticleFactory(injector);
        vertx.registerVerticleFactory(guiceVerticleFactory);

        VerticleWithVertxDependency.instanceCount.set(0);
    }

    /**
     * Verifies that verticle with dependency can be deployed successfully.
     * @throws Exception
     */
    @Test
    public void testDeployVerticle() throws Exception {
        // Act
        GuiceVertxDeploymentManager deploymentManager = new GuiceVertxDeploymentManager(this.vertx);
        deploymentManager.deployVerticle(VerticleWithVertxDependency.class);
        waitUntil(() -> VerticleWithVertxDependency.instanceCount.get() == 1);

        // Assert
        org.assertj.core.api.Assertions.assertThat(
            VerticleWithVertxDependency.instanceCount.get()).isGreaterThan(0);
    }

    /**
     * Verifies that verticle with dependency can be deployed successfully with specific deployment options.
     * @throws Exception
     */
    @Test
    public void testDeployVerticleWithOptions() throws Exception {
         // Act`
        GuiceVertxDeploymentManager deploymentManager = new GuiceVertxDeploymentManager(this.vertx);
        deploymentManager.deployVerticle(
            VerticleWithVertxDependency.class,
            new DeploymentOptions().setWorker(true));

        waitUntil(() -> VerticleWithVertxDependency.instanceCount.get() == 1);

        // Assert
        org.assertj.core.api.Assertions.assertThat(
            VerticleWithVertxDependency.instanceCount.get()).isGreaterThan(0);
    }

    /**
     * Verifies that verticle with dependency can be deployed successfully and result of the deployment can be received
     * via providing completion handler.
     *
     * @throws Exception
     */
    @Test
    public void testDeployVerticleWithOptionsAndCompletionHandler() throws Exception {
        // Act`
        ObservableFuture<String> deplymentResult = RxHelper.observableFuture();

        GuiceVertxDeploymentManager deploymentManager = new GuiceVertxDeploymentManager(this.vertx);
        deploymentManager.deployVerticle(
            VerticleWithVertxDependency.class,
            new DeploymentOptions(),
            deplymentResult.toHandler());

        String deploymentId = deplymentResult.toBlocking().single();

        // Assert
        org.assertj.core.api.Assertions.assertThat(deploymentId).isNotEmpty();
    }
}