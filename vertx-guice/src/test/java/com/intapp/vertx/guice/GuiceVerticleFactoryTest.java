package com.intapp.vertx.guice;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intapp.vertx.guice.stubs.*;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.impl.future.Listener;
import io.vertx.core.impl.future.PromiseImpl;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link GuiceVerticleFactory} class.
 */
@RunWith(VertxUnitRunner.class)
public class GuiceVerticleFactoryTest {
    @Rule public MockitoRule rule = MockitoJUnit.rule();//.strictness(Strictness.STRICT_STUBS);
    @Mock
    Vertx vertx;
    @Mock
    EventBus eventBus;
    @Mock
    FileSystem fileSystem;
    @Mock
    SharedData sharedData;

    private Injector injector;
    private GuiceVerticleFactory factory;

    @Before
    public void setUp(TestContext context) throws Exception {
        this.injector = Guice.createInjector(new DependencyModule());
        this.factory = new GuiceVerticleFactory(this.injector);
        this.factory.init(vertx);

        when(this.vertx.eventBus()).thenReturn(this.eventBus);
        when(this.vertx.fileSystem()).thenReturn(this.fileSystem);
        when(this.vertx.sharedData()).thenReturn(this.sharedData);
    }

    @Test
    public void testPrefix(TestContext context) {
        // Assert
        assertThat(factory.prefix()).isEqualTo("java-guice");
    }

    /**
     * This test verifies that Verticle which uses constructor injection for dependency management
     * can be instantiated successfully.
     */
    @Test
    public void testCreateVerticle_VerticleWithDependencyInConstructor_VerticleIsCreated(TestContext context) throws Exception {
        // Arrange
        Async async = context.async();
        String identifier = GuiceVerticleFactory.PREFIX + ":" + VerticleWithDependency.class.getName();
        Promise<Callable<Verticle>> promise = Promise.promise();
        promise.future().onComplete( result -> {
            try {
                Verticle verticle = result.result().call();
                // Assert
                assertThat(verticle).isExactlyInstanceOf(VerticleWithDependency.class);
                assertThat(((VerticleWithDependency) verticle).getDependency()).isExactlyInstanceOf(DependencyImpl.class);
                async.complete();
            } catch (Exception e) {
                fail("Error processing result", e);
            }
        });

        // Act
        factory.createVerticle(identifier, this.getClass().getClassLoader(), promise);
    }

    /**
     * This test verifies that {@link GuiceVertxLauncher} can be used to instantiate and inject dependency for
     * Uncompiled verticle (the same way as {@link io.vertx.core.impl.JavaVerticleFactory} works.
     */
    @Test
    public void testCreateVerticle_UncompiledVerticleWithDependencyInConstructor_VerticleIsCreated(TestContext context) {
        // Arrange
        Async async = context.async();
        this.injector = Guice.createInjector(new VertxModule(this.vertx));
        this.factory = new GuiceVerticleFactory(this.injector);
        this.factory.init(vertx);

        String identifier = GuiceVerticleFactory.PREFIX + ":UncompiledVerticleWithDependency.java";
        Promise<Callable<Verticle>> promise = Promise.promise();
        promise.future().onComplete( result -> {
            try {
                Verticle verticle = result.result().call();
                // Assert
                assertThat(verticle).isNotNull();
                async.complete();
            } catch (Exception e) {
                fail("Error processing result", e);
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
    public void testCreateVerticle_DependencyBindedInSingletonScope_SameInstanceIsUsedByAllVerticles(TestContext context) {
        // Arrange
        Async async = context.async();
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
                    async.complete();
                } catch (Exception e) {
                    fail("Error processing result", e);
                }
            }
        });
    }
}
