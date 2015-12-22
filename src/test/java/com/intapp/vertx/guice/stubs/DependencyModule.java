package com.intapp.vertx.guice.stubs;

import com.google.inject.AbstractModule;

/**
 * Guice module which binds {@link Dependency} interface to its default {@link DependencyImpl} implementation.
 */
public class DependencyModule extends AbstractModule
{
    @Override
    protected void configure() {
        this.bind(Dependency.class).to(DependencyImpl.class);
    }
}
