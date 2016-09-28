# RxRabbit: Reactive RabbitMQ client for Java
Highly available, reactive RabbitMQ client for the JVM.

### Reactive API
RxRabbit is a [RabbitMQ](https://www.rabbitmq.com/) java 8 client library that extends and enhances the [rabbitmq-java-client](https://www.rabbitmq.com/java-client.html) by providing a resilient, auto-connecting [ReactiveX](http://reactivex.io/) styled API.

Consuming messages from an AMQP queue is by its nature a reactive flow, and therefore RxRabbit exposes consumed messages as an [rx.Observable](http://reactivex.io/documentation/observable.html) which can be subscribed to.

Publishing messages is modeled as a rx.functions.Func4 that returns an [rx.Single](http://reactivex.io/documentation/single.html) when its done.

### Robust and simple error handling with recovery
The [official rabbitmq Java client](https://github.com/rabbitmq/rabbitmq-java-client) provides some basic but non-complete error handling and recovery mechanisms. 
A number of wrapper libraries already exists today which, with varying success, automatically handles connection recovery in more error scenarios than the official java client.
The best ones we have found so far were [Spring-amqp](http://projects.spring.io/spring-amqp/) which we we discovered suffered from excessive and erroneous channel handling and
[lyra](https://github.com/jhalterman/lyra) which, in our view, implements and overly complex error handling logic that still has issues with recovering from some extreme error cases, such as hard broker restarts.

RxRabbit instead uses a very basic but effective approach when it comes to error handling and recovery:

**No matter the error, the code assumes that the connection is broken and starts to repeatedly attempt to re-connect to the broker with exponential back-off.**

Consumers and producers do not have to care about underlying rabbit channels and connections as reconnects will remain hidden from the API user unless specifically asked for.

### Opinionated API
The API has strong opinions on how RabbitMQ should be used. It hides a lot of functionality from the Channel interface and also introduces a concept of **channel types** that are made for a specific purpose (such as publish, consume or admin operations). This means of course that there are several things that you can't do with this api, but keep in mind that it is a conscious decision made by the API developers.

Main supported use cases:

- Continuously 'infinite' consume (with manual acknowledgment) from an already existing queue on the rabbit broker.
- Continuously 'infinite' consume (with manual acknowledgment) from a server created, temporary, exclusive queue bound to an existing exchange on the rabbit broker.
- Publish messages to an exchange with (or without) publisher confirmation but with mandatory=false and immediate=false.
- Perform basic 'admin' operations (declare, remove and purge queues, exchanges and bindings)


## How to use it

### Dependencies

*Gradle:*

```groovy
    
repositories {
   jcenter()
}
...

dependencies {
    compile 'com.meltwater:rxrabbit:<insert-version>'
    ...
}
```
       
*Maven:*

```xml  
<repositories>
   <repository>
      <id>jcenter</id>
      <url>http://jcenter.bintray.com</url>
      <snapshots>
        <enabled>true</enabled>
        <updatePolicy>never</updatePolicy>
        <checksumPolicy>warn</checksumPolicy>
      </snapshots>
       <releases>
         <enabled>true</enabled>
         <checksumPolicy>warn</checksumPolicy>
      </releases>
   </repository>
   ...
</repositories>
...

<dependency>
  <groupId>com.meltwater</groupId>
  <artifactId>rxrabbit</artifactId>
  <version><insert-version></version>
  <type>jar</type>
  ...
</dependency>
```

### Code examples

The best way to get a feel for how the API should be used have a look at the [example-app](example-app).

It is also useful to look at the [integration tests](rxrabbit/src/test/groovy/com/meltwater/rxrabbit/RxRabbitTests.java) and the [ExampleCode class](rxrabbit/src/test/java/com/meltwater/rxrabbit/example/ExampleCode.java) class.

You can of course also have a look at the javadoc as most core API classes and interfaces are reasonably well documented.

### Build and run tests

Build by running `./gradlew clean build`.

Note that the build will use [docker](https://www.docker.com/) when running the tests, so you need to have docker v1.8 or later installed for the build to succeed.

Also note that it is currently NOT supported to run the test on OSX using docker machine, you need to be able to connect to docker containers using localhost:<port>. 


