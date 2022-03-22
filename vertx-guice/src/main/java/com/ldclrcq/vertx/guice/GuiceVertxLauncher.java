package com.ldclrcq.vertx.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;

import java.util.LinkedList;
import java.util.List;

/**
 * Extends {@link Launcher} to use {@link GuiceVerticleFactory} verticle factory which uses Guice for verticle
 * creation.
 *
 * <p>Note: Verticle should be deployed with the factory prefix {@link GuiceVerticleFactory#PREFIX} to make vertx
 * to use registered factory for verticle creation and dependency injection.</p>
 */
public class GuiceVertxLauncher extends Launcher {

    /**
     * Main entry point.
     *
     * @param args the user command line arguments. For supported command line arguments please see {@link Launcher}.
     */
    public static void main(String[] args) {
        new GuiceVertxLauncher().dispatch(args);
    }

    @Override
    public void afterStartingVertx(Vertx vertx) {
        super.afterStartingVertx(vertx);

        GuiceVerticleFactory guiceVerticleFactory =
            new GuiceVerticleFactory(this.createInjector(vertx));
        vertx.registerVerticleFactory(guiceVerticleFactory);
    }

    /**
     * Creates injector to be used for verticle creation.
     * This injector instance will be shared by all Verticles.
     *
     * <p>Override this method if you want to customize logic of the injector creation.
     * To add application specific modules to be used by injector, {@link #getModules} method should
     * be overridden instead.</p>
     *
     * @param vertx Vert.x instance for which injector will be created.
     * @return Guice injector instantiated with modules provided by {@link #getModules} method.
     */
    protected Injector createInjector(Vertx vertx) {
        return Guice.createInjector(this.getModules(vertx));
    }

    /**
     * Gets the collection of the Guice modules to be registered in Guice injector.
     * Override this method to add application specific modules.
     *
     * <p>By default returned collection contains only {@link VertxModule} module.</p>
     *
     * @param vertx Vert.x instance to load modules into.
     * @return Collection of the modules to register in injector.
     */
    protected List<Module> getModules(Vertx vertx) {
        List<Module> modules = new LinkedList<>();
        modules.add(new VertxModule(vertx));
        return modules;
    }
}
