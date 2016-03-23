/*
 * Created by IntelliJ IDEA.
 * User: Maxim
 * Date: 3/23/2016
 * Time: 8:47 PM
 */
package com.intapp.vertx.guice.examples.serviceproxy.business;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class BusinessModule extends AbstractModule {
    protected void configure() {
        this.bind(BusinessService.class).toProvider(BusinessServiceProvider.class).in(Singleton.class);
    }
}
