package com.intapp.vertx.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intapp.vertx.guice.stubs.*;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link GuiceVerticleFactory} class.
 */
@ExtendWith(VertxExtension.class)
public class GuiceVerticleFactoryTest {


    private Injector injector;
    private GuiceVerticleFactory factory;

    @BeforeEach
    public void setUp(Vertx vertx) {

        this.injector = Guice.createInjector(new DependencyModule());
        this.factory = new GuiceVerticleFactory(this.injector);
        this.factory.init(vertx);
    }

    @Test
    public void testPrefix() {
        // Assert
        assertThat(factory.prefix()).isEqualTo("java-guice");
    }

    /**
     * This test verifies that Verticle which uses constructor injection for dependency management
     * can be instantiated successfully.
     */
    @Test
    public void testCreateVerticle_VerticleWithDependencyInConstructor_VerticleIsCreated(VertxTestContext context) {
        // Arrange
        String identifier = GuiceVerticleFactory.PREFIX + ":" + VerticleWithDependency.class.getName();
        Promise<Callable<Verticle>> promise = Promise.promise();
        promise.future().onComplete( result -> {
            try {
                Verticle verticle = result.result().call();
                // Assert
                assertThat(verticle).isExactlyInstanceOf(VerticleWithDependency.class);
                assertThat(((VerticleWithDependency) verticle).getDependency()).isExactlyInstanceOf(DependencyImpl.class);
                context.completeNow();
            } catch (Exception e) {
                context.failNow(e);
            }
        });

        // Act
        factory.createVerticle(identifier, this.getClass().getClassLoader(), promise);
    }

    /**
     * This test verifies that when dependency is injected using Singleton scope,
     * than the single instance of this dependency is used by all verticles.
     */
    @Test
    public void testCreateVerticle_DependencyBindedInSingletonScope_SameInstanceIsUsedByAllVerticles(Vertx vertx, VertxTestContext context) {
        // Arrange
        this.injector = Guice.createInjector(new DependencyAsSingletonModule());
        this.factory = new GuiceVerticleFactory(this.injector);
        this.factory.init(vertx);

        String identifier = GuiceVerticleFactory.PREFIX + ":" + VerticleWithDependency.class.getName();
        String identifier2 = GuiceVerticleFactory.PREFIX + ":" + Verticle2WithDependency.class.getName();
        Promise<Callable<Verticle>> promise1 = Promise.promise();
        Promise<Callable<Verticle>> promise2 = Promise.promise();

        // Act
        factory.createVerticle(identifier, this.getClass().getClassLoader(), promise1);
        factory.createVerticle(identifier2, this.getClass().getClassLoader(), promise2);

        CompositeFuture.all(promise1.future(), promise2.future()).onComplete(result -> {
            if (result.succeeded()) {
                List<Callable<?>> list = result.result().list();
                try {
                    VerticleWithDependency verticle1 = (VerticleWithDependency) list.get(0).call();
                    Verticle2WithDependency verticle2 = (Verticle2WithDependency) list.get(1).call();
                    // Assert
                    assertThat(verticle1.getDependency()).isSameAs(verticle2.getDependency());
                    context.completeNow();
                } catch (Exception e) {
                    context.failNow(e);
                }
            }
        });
    }
}
