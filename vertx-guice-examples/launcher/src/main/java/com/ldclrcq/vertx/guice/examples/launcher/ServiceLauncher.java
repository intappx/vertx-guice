package com.ldclrcq.vertx.guice.examples.launcher;

import com.ldclrcq.vertx.guice.GuiceVertxLauncher;
import com.ldclrcq.vertx.guice.examples.launcher.business.BusinessModule;

import com.google.inject.Module;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;

import java.util.List;

/**
 * Examples which shows the following:
 * 1) Usage of the Vertx Launcher and Gradle configuration
 * 2) Extending GuiceVertxLauncher to register application specific modules
 */
public class ServiceLauncher extends GuiceVertxLauncher {
    /**
     * Main entry point.
     *
     * @param args the user command line arguments. For supported command line arguments please see {@link Launcher}.
     */
    public static void main(String[] args) {
        new ServiceLauncher().dispatch(args);
    }

    @Override
    protected List<Module> getModules(Vertx vertx) {
        List<Module> modules = super.getModules(vertx);
        modules.add(new BusinessModule());
        return modules;
    }
}
