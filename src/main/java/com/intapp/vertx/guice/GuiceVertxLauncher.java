package com.intapp.vertx.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;

import java.util.LinkedList;
import java.util.List;

/**
 * Extends {@link Launcher} to use {@link @GuiceVerticleFactory} verticle factory which uses Guice for verticle creation.
 *
 * Note: Verticle should be deployed with the factory prefix {@link GuiceVerticleFactory::PREFIX} to make vertx
 * to use registered factory for verticle creation and dependency injection.
 */
public class GuiceVertxLauncher extends Launcher {
    @Override
    public void afterStartingVertx(Vertx vertx) {
        super.afterStartingVertx(vertx);

        GuiceVerticleFactory guiceVerticleFactory =
            new GuiceVerticleFactory(this.createInjector(vertx));
        vertx.registerVerticleFactory(guiceVerticleFactory);
    }

    /**
     * Main entry point.
     *
     * @param args the user command line arguments. For supported command line arguments please see {@link Launcher}.
     */
    public static void main(String[] args) {
        new GuiceVertxLauncher().dispatch(args);
    }

    /**
     * Creates injector to be used for verticle creation.
     * This injector instance will be shared by all Verticles.
     *
     * Override this method if you want to customize logic of the injector creation.
     * To add application specific modules to be used by injector, {@link GuiceVertxLauncher::getModules} method should
     * be overridden instead.
     */
    protected Injector createInjector(Vertx vertx)
    {
        return Guice.createInjector(this.getModules(vertx));
    }

    /**
     * Gets the collection of the Guice modules to be registered in Guice injector.
     * Override this method to add application specific modules.
     *
     * By default returned collection contains only {@link VertxModule} module.
     */
    protected List<Module> getModules(Vertx vertx)
    {
        List<Module> modules = new LinkedList<>();
        modules.add(new VertxModule(vertx));
        return modules;
    }
}
