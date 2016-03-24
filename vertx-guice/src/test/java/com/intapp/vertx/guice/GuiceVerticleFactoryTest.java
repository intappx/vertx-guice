package com.intapp.vertx.guice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.intapp.vertx.guice.stubs.DependencyAsSingletonModule;
import com.intapp.vertx.guice.stubs.DependencyImpl;
import com.intapp.vertx.guice.stubs.DependencyModule;
import com.intapp.vertx.guice.stubs.Verticle2WithDependency;
import com.intapp.vertx.guice.stubs.VerticleWithDependency;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.shareddata.SharedData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link GuiceVerticleFactory} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class GuiceVerticleFactoryTest {
    @Mock Vertx vertx;
    @Mock EventBus eventBus;
    @Mock FileSystem fileSystem;
    @Mock SharedData sharedData;

    private Injector injector;
    private GuiceVerticleFactory factory;

    @Before
    public void setUp() throws Exception {
        this.injector = Guice.createInjector(new DependencyModule());
        this.factory = new GuiceVerticleFactory(this.injector);
        this.factory.init(vertx);

        when(this.vertx.eventBus()).thenReturn(this.eventBus);
        when(this.vertx.fileSystem()).thenReturn(this.fileSystem);
        when(this.vertx.sharedData()).thenReturn(this.sharedData);
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
    public void testCreateVerticle_VerticleWithDependencyInConstructor_VerticleIsCreated() throws Exception {
        // Arrange
        String identifier = GuiceVerticleFactory.PREFIX + ":" + VerticleWithDependency.class.getName();

        // Act
        Verticle verticle = factory.createVerticle(identifier, this.getClass().getClassLoader());

        // Assert
        assertThat(verticle).isExactlyInstanceOf(VerticleWithDependency.class);
        assertThat(((VerticleWithDependency) verticle).getDependency()).isExactlyInstanceOf(DependencyImpl.class);
    }

    /**
     * This test verifies that {@link GuiceVertxLauncher} can be used to instantiate and inject dependency for
     * Uncompiled verticle (the same way as {@link io.vertx.core.impl.JavaVerticleFactory} works.
     */
    @Test
    public void testCreateVerticle_UncompiledVerticleWithDependencyInConstructor_VerticleIsCreated() throws Exception {
        // Arrange
        this.injector = Guice.createInjector(new VertxModule(this.vertx));
        this.factory = new GuiceVerticleFactory(this.injector);
        this.factory.init(vertx);

        String identifier = GuiceVerticleFactory.PREFIX + ":UncompiledVerticleWithDependency.java";

        // Act
        Verticle verticle = factory.createVerticle(identifier, this.getClass().getClassLoader());

        // Assert
        assertThat(verticle).isNotNull();
    }

    /**
     * This test verifies that when dependency is injected using Singleton scope,
     * than the single instance of this dependency is used by all verticles.
     */
    @Test
    public void testCreateVerticle_DependencyBindedInSingletonScope_SameInstanceIsUsedByAllVerticles()
        throws Exception {
        // Arrange
        this.injector = Guice.createInjector(new DependencyAsSingletonModule());
        this.factory = new GuiceVerticleFactory(this.injector);
        this.factory.init(vertx);

        String identifier = GuiceVerticleFactory.PREFIX + ":" + VerticleWithDependency.class.getName();
        String identifier2 = GuiceVerticleFactory.PREFIX + ":" + Verticle2WithDependency.class.getName();

        // Act
        VerticleWithDependency verticle =
            (VerticleWithDependency) factory.createVerticle(identifier, this.getClass().getClassLoader());
        Verticle2WithDependency verticle2 =
            (Verticle2WithDependency) factory.createVerticle(identifier2, this.getClass().getClassLoader());

        // Assert
        assertThat(verticle.getDependency()).isSameAs(verticle2.getDependency());
    }
}
