package com.ldclrcq.vertx.guice.examples.launcher.business;

class DependencyImpl implements Dependency {
    @Override
    public String getGreetingMessage() {
        return "Hi all from vertx-guice launcher example.";
    }
}
