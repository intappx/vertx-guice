package com.intapp.vertx.guice.stubs;

import com.google.common.base.Preconditions;

import io.vertx.core.AbstractVerticle;

import javax.inject.Inject;

/**
 * Verticle which uses constructor injection to define dependent classes.
 */
public class Verticle2WithDependency extends AbstractVerticle {
    private final Dependency dependency;

    @Inject
    public Verticle2WithDependency(Dependency dependency) {
        this.dependency = Preconditions.checkNotNull(dependency);
    }

    public Dependency getDependency() {
        return dependency;
    }
}
