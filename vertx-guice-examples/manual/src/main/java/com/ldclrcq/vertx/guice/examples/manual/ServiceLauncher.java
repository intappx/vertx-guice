package com.ldclrcq.vertx.guice.examples.manual;

import com.ldclrcq.vertx.guice.GuiceVerticleFactory;
import com.ldclrcq.vertx.guice.GuiceVertxDeploymentManager;
import com.ldclrcq.vertx.guice.VertxModule;
import com.ldclrcq.vertx.guice.examples.manual.business.BusinessModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * Example which shows the following:
 * 1) Creating vertx manually in application main entry point
 * 2) Creating single injector with Vertx and application specific modules
 * 3) Registering GuiceVerticleFactory.
 * 4) Deploying verticle programmatically which uses GuiceVerticleFactory
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

        GuiceVertxDeploymentManager deploymentManager = new GuiceVertxDeploymentManager(vertx);
        deploymentManager.deployVerticle(MainVerticle.class);
    }
}
