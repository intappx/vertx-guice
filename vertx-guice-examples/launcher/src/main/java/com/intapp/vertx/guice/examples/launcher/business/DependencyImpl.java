package com.intapp.vertx.guice.examples.launcher.business;

class DependencyImpl implements Dependency {
    @Override
    public String getGreetingMessage() {
        return "Hi all from vertx-guice launcher example.";
    }
}
