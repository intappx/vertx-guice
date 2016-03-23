package com.intapp.vertx.guice.examples.serviceproxy;

import com.intapp.vertx.guice.GuiceVerticleFactory;
import com.intapp.vertx.guice.GuiceVertxDeploymentManager;
import com.intapp.vertx.guice.VertxModule;
import com.intapp.vertx.guice.examples.serviceproxy.business.BusinessModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * Example which shows the following:
 * 1) Particullar the same stuff from manual example with addition that Vertx service concept is used.
 * 2) Dependency injection in services
 * 3) Dependency inejction of the proxy to services.
 */
public class ServiceLauncher {
    /**
     * Main entry point.
     *
     * @param args the user command line arguments. For supported command line arguments please see {@link Launcher}.
     */
    public static void main(String[] args) {
        new ServiceLauncher().launch();
    }

    public void launch() {
        Vertx vertx = Vertx.vertx(new VertxOptions());

        Injector injector = Guice.createInjector(
            new VertxModule(vertx),
            new BusinessModule());

        GuiceVerticleFactory guiceVerticleFactory = new GuiceVerticleFactory(injector);
        vertx.registerVerticleFactory(guiceVerticleFactory);

        GuiceVertxDeploymentManager.deployVerticle(vertx, MainVerticle.class);
    }
}
