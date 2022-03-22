package com.ldclrcq.vertx.guice.examples.serviceproxy;

import com.ldclrcq.vertx.guice.examples.serviceproxy.business.BusinessService;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import com.ldclrcq.vertx.guice.VertxModule;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * Implements verticle to show dependency injection in action.
 * The following dependencies are injected:
 * 1) Vertx instance which bindings are configured in {@link VertxModule}
 * 2) Application specific dependency which is proxy to {@link BusinessService} class.
 */
public class MainVerticle extends AbstractVerticle{
    private final BusinessService businessService;
    private final Vertx vertx;

    @Inject
    public MainVerticle(final Vertx vertx, final BusinessService businessService) {
        this.businessService = Preconditions.checkNotNull(businessService);
        this.vertx = Preconditions.checkNotNull(vertx);
    }

    @Override
    public void start() throws Exception {
        super.start();

        this.vertx.createHttpServer()
            .requestHandler(requestHandler ->
                this.businessService.getGreetingMessage(handler ->
                    requestHandler.response().end(handler.result())))
            .listen(8080, handler -> {
                if (handler.succeeded()) {
                    System.out.println("http://localhost:8080/");
                } else {
                    System.err.println("Failed to listen on port 8080");
                }
            });
    }
}
