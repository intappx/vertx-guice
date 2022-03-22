package com.intapp.vertx.guice;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;

import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.impl.verticle.CompilingClassLoader;
import io.vertx.core.spi.VerticleFactory;

import java.util.concurrent.Callable;

/**
 * Represents verticle factory which uses Guice for verticle creation.
 * To make vertx to use this factory for verticle creation the following criteria should be accomplished:
 * 1) This factory should be registered in Vertx. One of the way to achieve this is to use {@link GuiceVertxLauncher}
 * as Vertx main launcher.
 * 2) Verticle should be deployed with the factory prefix {@link #PREFIX}.
 */
public class GuiceVerticleFactory implements VerticleFactory {
    public static final String PREFIX = "java-guice";
    private final Injector injector;

    public GuiceVerticleFactory(Injector injector) {
        this.injector = Preconditions.checkNotNull(injector);
    }

    @Override
    public String prefix() {
        return PREFIX;
    }

    @Override
    public void createVerticle(String verticleName, ClassLoader classLoader, Promise<Callable<Verticle>> promise) {
        verticleName = VerticleFactory.removePrefix(verticleName);
        Class<Verticle> clazz;

        try {
            if (verticleName.endsWith(".java")) {
                CompilingClassLoader compilingLoader = new CompilingClassLoader(classLoader, verticleName);
                String className = compilingLoader.resolveMainClassName();
                clazz = (Class<Verticle>) compilingLoader.loadClass(className);
            } else {
                clazz = (Class<Verticle>) classLoader.loadClass(verticleName);
            }
            Verticle verticle = this.injector.getInstance(clazz);
            promise.complete(() -> verticle);
        } catch (ClassNotFoundException e) {
            promise.fail(e);
        }
    }

}
