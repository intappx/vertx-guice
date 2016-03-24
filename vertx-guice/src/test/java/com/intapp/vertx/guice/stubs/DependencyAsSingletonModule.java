package com.intapp.vertx.guice.stubs;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class DependencyAsSingletonModule extends AbstractModule {
    protected void configure() {
        this.bind(Dependency.class).to(DependencyImpl.class).in(Singleton.class);
    }
}
