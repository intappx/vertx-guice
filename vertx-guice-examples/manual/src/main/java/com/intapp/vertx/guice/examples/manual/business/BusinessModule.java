package com.intapp.vertx.guice.examples.manual.business;


import com.google.inject.AbstractModule;

public class BusinessModule extends AbstractModule {
    protected void configure() {
        this.bind(Dependency.class).to(DependencyImpl.class);
    }
}
