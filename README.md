# vertx-guice
Enable Verticle dependency injection in Vert.x using Guice. 

This project is a fork of [intappx/vertx-guice](https://github.com/intappx/vertx-guice) which is not maintained anymore

[![Build Status](https://travis-ci.org/intappx/vertx-guice.svg?branch=master)](https://travis-ci.org/intappx/vertx-guice)
[![Code Coverage](https://img.shields.io/codecov/c/github/intappx/vertx-guice.svg)](https://codecov.io/github/intappx/vertx-guice)
[![Maven Central](https://img.shields.io/maven-central/v/com.intapp/vertx-guice.svg?maxAge=2592000)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.intapp%22%20AND%20a%3A%22vertx-guice%22)

It is designed to use single injector per Vert.x instance.
It means that `Singleton' scope is supported and works as expected. This was the main reason to implement this library instead of using [vertx-guice](https://github.com/englishtown/vertx-guice) library from English Town.

### Compatibility :

- Vertx 4
- Guice 5
- Java 11+

### What does it provide
* `GuiceVerticleFactory` which uses Guice for verticle creation. To be used, it should be registered in Vertx and verticle should be deployed with `java-guice:` prefix. 
* `GuiceVertxLauncher` which extends default [vert.x launcher](http://vertx.io/docs/vertx-core/java/#_the_vert_x_launcher). It performs all necessary work related to the creating single injector per Vert.x instance, registering `GuiceVerticleFactory`. 
To register application specific dependencies, you can also create a sub-class of `GuiceVertxLauncher` and use it to start your application.
* `GuiceVertxDeploymentManager` which implements convenient methods to deploy verticles programmatically by specified class using `GuiceVerticleFactory` factory.
* `VertxModule` which contains binding for vertx itself and it's cached objects like EventBus, FileSystem, SharedData.

### How to use
Several examples were added which covers main scenarios of the adding dependency injection support. 

###### [Vert.x Launcher](vertx-guice-examples/launcher)
Shows the way to use `GuiceVertxLauncher` to run application with registering application specific modules and deploying verticle with injecting dependencies.

Basically it covers the following:

* Extending `GuiceVertxLauncher` to register application specific modules
* Customized `run` gradle task which deploys verticle with constructor dependency injection. 

*Note* some of the useful information about Vert.x launcher and gradle configuration can be found in [vertx 3.0 examples](https://github.com/vert-x3/vertx-examples/tree/master/gradle-redeploy).

##### [Creating and running Vert.x manually](vertx-guice-examples/manual)
Example which shows the following:

* Creating vertx manually in application main entry point
* Creating single injector with Vertx and application specific modules
* Registering 'GuiceVerticleFactory'.
* Deploying verticle programmatically which uses 'GuiceVerticleFactory'

##### [Using dependency injection with Vert.x services](vertx-guice-examples/service-proxy)
There is an [Vert.x Service Proxy](http://vertx.io/docs/vertx-service-proxy/java/) library which allows to develop less verbosity code for asynchronous verticles (which works over event bus).
This example shows the following:

* Particular the same stuff from manual example with addition that Vertx service concept is used.
* Dependency injection in service itself
* Hiding service registration and proxy creation by using Guice Provider concept
* Using of the service via injected generated proxy in verticle 
* Gradle configuration

### TO-DO
* Request Scope Support (if there will be a real use case for this)
