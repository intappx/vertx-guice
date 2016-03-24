package com.intapp.vertx.guice.examples.serviceproxy.business;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

class BusinessServiceImpl implements BusinessService {
    private final Vertx vertx;

    @Inject
    public BusinessServiceImpl(final Vertx vertx) {
        // This is done for demonstration purpose only to show that service dependency can be resolved too.
        this.vertx = Preconditions.checkNotNull(vertx);
    }

    @Override
    public void getGreetingMessage(Handler<AsyncResult<String>> resultHandler) {
        resultHandler.handle(Future.succeededFuture("Hi all from vertx-guice from service."));
    }
}
