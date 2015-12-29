package com.intapp.vertx.guice.stubs;

import com.google.common.base.Preconditions;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

public class VerticleWithVertxDependency extends AbstractVerticle {
    public static AtomicInteger instanceCount = new AtomicInteger();

    @Inject
    public VerticleWithVertxDependency(Vertx vertxInstanceDependency) {
        Preconditions.checkNotNull(vertxInstanceDependency);
    }

    @Override
    public void start() throws Exception {
        instanceCount.incrementAndGet();
    }

    @Override
    public void stop() throws Exception {
        instanceCount.decrementAndGet();
    }
}
