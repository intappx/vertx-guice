package com.ldclrcq.vertx.guice;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.shareddata.SharedData;

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
        bind(EventBus.class).toInstance(this.vertx.eventBus());
        bind(FileSystem.class).toInstance(this.vertx.fileSystem());
        bind(SharedData.class).toInstance(this.vertx.sharedData());
    }
}
