/*
 * Created by IntelliJ IDEA.
 * User: Maxim
 * Date: 3/23/2016
 * Time: 10:30 PM
 */
package com.ldclrcq.vertx.guice.examples.serviceproxy.business;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;

import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * Guice provider which return code generation proxy to {@link BusinessService} service.
 * It also registered real service implementation. This way all registration is hidden from consumer.
 */
class BusinessServiceProvider implements Provider<BusinessService> {
    private final Vertx vertx;

    @Inject
    public BusinessServiceProvider(Vertx vertx) {
        this.vertx = Preconditions.checkNotNull(vertx);
    }

    public BusinessService get() {
        // Register service implementation
        BusinessService service = new BusinessServiceImpl(vertx);
        ProxyHelper.registerService(
            BusinessService.class, this.vertx, service, BusinessService.class.getCanonicalName());

        // All other code should work via service proxy
        return ProxyHelper.createProxy(BusinessService.class, this.vertx, BusinessService.class.getCanonicalName());
    }
}
