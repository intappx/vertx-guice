package com.ldclrcq.vertx.guice.examples.launcher;

import com.ldclrcq.vertx.guice.VertxModule;
import com.ldclrcq.vertx.guice.examples.launcher.business.Dependency;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * Implements verticle to show dependency injection in action.
 * The following dependencies are injected:
 * 1) Vertx instance which bindings are configured in {@link VertxModule}
 * 2) Application specific dependency
 */
public class MainVerticle extends AbstractVerticle{
    private final Dependency dependency;
    private final Vertx vertx;

    @Inject
    public MainVerticle(final Vertx vertx, final Dependency dependency) {
        this.dependency = Preconditions.checkNotNull(dependency);
        this.vertx = Preconditions.checkNotNull(vertx);
    }

    @Override
    public void start() throws Exception {
        super.start();

        this.vertx.createHttpServer()
            .requestHandler(req -> req.response().end(this.dependency.getGreetingMessage()))
            .listen(8080, handler -> {
                if (handler.succeeded()) {
                    System.out.println("http://localhost:8080/");
                } else {
                    System.err.println("Failed to listen on port 8080");
                }
            });
    }
}
