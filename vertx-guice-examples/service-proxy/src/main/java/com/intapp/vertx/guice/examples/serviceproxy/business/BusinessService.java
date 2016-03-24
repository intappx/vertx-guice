package com.intapp.vertx.guice.examples.serviceproxy.business;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

@ProxyGen
public interface BusinessService {
    void getGreetingMessage(Handler<AsyncResult<String>> resultHandler);
}
