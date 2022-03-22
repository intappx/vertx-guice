package com.ldclrcq.vertx.guice.examples.serviceproxy.business;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class BusinessModule extends AbstractModule {
    protected void configure() {
        this.bind(BusinessService.class).toProvider(BusinessServiceProvider.class).in(Singleton.class);
    }
}
