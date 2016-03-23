/*
 * Created by IntelliJ IDEA.
 * User: Maxim
 * Date: 3/23/2016
 * Time: 8:47 PM
 */
package com.intapp.vertx.guice.examples.launcher.business;


import com.google.inject.AbstractModule;

public class BusinessModule extends AbstractModule {
    protected void configure() {
        this.bind(Dependency.class).to(DependencyImpl.class);
    }
}
