package com.intapp.vertx.guice;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;

import io.vertx.core.Vertx;

/**
 * Guice {@link AbstractModule} for vertx and container injections.
 */
public class VertxModule extends AbstractModule {

    private final Vertx vertx;

    public VertxModule(Vertx vertx) {
        this.vertx = Preconditions.checkNotNull(vertx);
    }

    @Override
    protected void configure() {
        bind(Vertx.class).toInstance(this.vertx);
    }
}
