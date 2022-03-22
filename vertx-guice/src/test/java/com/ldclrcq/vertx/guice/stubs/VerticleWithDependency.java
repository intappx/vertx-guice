package com.ldclrcq.vertx.guice.stubs;

import com.google.common.base.Preconditions;

import io.vertx.core.AbstractVerticle;

import javax.inject.Inject;

/**
 * Verticle which uses constructor injection to define dependent classes.
 */
public class VerticleWithDependency extends AbstractVerticle {
    private final Dependency dependency;

    @Inject
    public VerticleWithDependency(Dependency dependency) {
        this.dependency = Preconditions.checkNotNull(dependency);
    }

    public Dependency getDependency() {
        return dependency;
    }
}

