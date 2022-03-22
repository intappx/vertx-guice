package com.ldclrcq.vertx.guice.examples.manual.business;

class DependencyImpl implements Dependency {
    @Override
    public String getGreetingMessage() {
        return "Hi all from vertx-guice manual example.";
    }
}
