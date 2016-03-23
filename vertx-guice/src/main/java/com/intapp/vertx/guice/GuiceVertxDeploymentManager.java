package com.intapp.vertx.guice;

import com.google.common.base.Preconditions;

import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

/**
 * Implements convenient methods to deploy verticles programmatically by specified class using @{@link GuiceVerticleFactory} factory.
 */
public class GuiceVertxDeploymentManager {
    /**
     * Deploy a verticle instance given a class of the verticle using default deployment options
     * and @{@link GuiceVerticleFactory} factory.
     *
     * @param vertx The vertx instance to deploy verticle into.
     * @param verticleClazz the class of the verticle to deploy.
     */
    public static void deployVerticle(final Vertx vertx, final Class verticleClazz) {
        Preconditions.checkNotNull(vertx);
        Preconditions.checkNotNull(verticleClazz);

        deployVerticle(vertx, verticleClazz, new DeploymentOptions());
    }

    /**
     * Like {@link .deployVerticle(Vertx, Class)} but {@link io.vertx.core.DeploymentOptions} are provided to configure the
     * deployment.
     *
     * @param vertx The vertx instance to deploy verticle into.
     * @param verticleClazz  the class of the verticle to deploy.
     * @param options  the deployment options.
     */
    public static void deployVerticle(final Vertx vertx, final Class verticleClazz, final DeploymentOptions options) {
        Preconditions.checkNotNull(vertx);
        Preconditions.checkNotNull(verticleClazz);
        Preconditions.checkNotNull(options);

        vertx.deployVerticle(getFullVerticleName(verticleClazz), options);
    }

    /**
     * Like {@link .deployVerticle(Vertx, Class, Handler )} but {@link io.vertx.core.DeploymentOptions} are provided to configure the
     * deployment.
     *
     * @param vertx The vertx instance to deploy verticle into.
     * @param verticleClazz  the class of the verticle to deploy.
     * @param options  the deployment options.
     * @param completionHandler  a handler which will be notified when the deployment is complete.
     */
    public static void deployVerticle(final Vertx vertx, final Class verticleClazz, final DeploymentOptions options, Handler<AsyncResult<String>> completionHandler) {
        Preconditions.checkNotNull(vertx);
        Preconditions.checkNotNull(verticleClazz);
        Preconditions.checkNotNull(options);
        Preconditions.checkNotNull(completionHandler);

        vertx.deployVerticle(getFullVerticleName(verticleClazz), options, completionHandler);
    }

    /**
     * Gets the name of the verticle with adding prefix required to notify vertx to use
     * @{@link GuiceVerticleFactory} factory for verticle creation.
     *
     * @param verticleClazz the class of the verticle to deploy.
     * @return Name of the verticle which can be used for deployment to vertx.
     */
    private static String getFullVerticleName(final Class verticleClazz) {
        return GuiceVerticleFactory.PREFIX + ":" + verticleClazz.getCanonicalName();
    }
}