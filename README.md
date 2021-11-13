# EventBusJava

This library provides a way to publish your own events and subscribe to them in a easy and powerful way.

* [Set up](#set-up)
* [Event class](#event)
* [Publish events](#publisher)
* [Listen to events](#listeners)
  * [Listen to events main class](#listen-to-main-class)
  * [Rollback](#rollback)
  * [Transactions](#transactional-event)
  * [Priority](#priority)
  * [Listen certain types of events](#listen-to-certain-types-of-events)


[click here to see transactional event](#transactional-event

## SET UP
**Repository**
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

**Dependency**
```xml
<dependency>
    <groupId>com.github.JaimeTruman</groupId>
    <artifactId>EventBusJava</artifactId>
    <version>Tag</version>
</dependency>
```

## EVENT

Each event will be represented by a class. To create you own event you have to extend Event. 

Event classs atributes:
* LocalDateTime timeOnCreated time when the event was instantiaded
* UUID eventUuid generated randomly

Event methods overridable (they are optional)
* String getEventName() default "" name of the event
* boolean isTransactional() default false [click here to see transactional event](#transactional-event)

```java
@AllArgsConstructor
public class GameTimedOutEvent extends Event {
    @Getter private final String team1;
    @Getter private final String tema2;
    
    @Override
    public String getEventName(){
        return "game.gametimedout";
    }
    
    @Override
    public boolean isTransactional(){
        return true;
    }
}
```

## PUBLISHER

To publish an event you have EventBusSynch. When a event is published, its subscriber will be executed in the same thread. This implements EventBus interface.
It is recommended to have only one eventpublisher in memory.

Constructor parameters:
* String packageToScan package to look for your event listeners:

```java
EventBus eventbus = new EventBusSynch("es.jaimetruman");

eventbus.publish(new GameTimedOutEvent("team 1", "team 2"));
```

## LISTENERS

Each event listener will be represented by a method. To make this possible, the methods needs:

* Be annotated with @EventListener
* Have only one param, the Event object

!Important! The class that has the method needs to have at least one public empty constructor. The method name doesn't matter.

```java
@EventListener
public void on(GameTimedOutEvent event) {
    System.out.printf("Game has ended beacause of time out %s %s", event.getTeam1(), event.getTeam2());
}
```
### Listen to main class

If you want to listen a main class event. You can simply put the main class name in the mehtod's parameter. Example:

A extends Event

B extends A

```java
eventbus.publish(new A());
eventbus.publish(new B());
```
This will get executed two times:

```java
@EventListener
public void on(A event) {
    //It will listen all events of type and subtype of A
}
```

### Rollback

If your event listener fails when it is getting executed the event manager will call rollback() method if you have implemented TransactionalEventListener

```java
public class EventListener implements TransactionalEventListener{
    @EventListener
    public void on(A event){
        Integer.parseInt("this will fail");
    }
    
    @Override
    public void rollback(){
        //This will be executed
    }
}
```

### Transactional event

If some of your eventlistener fails, maybe you want the event manager to call to each event listener to rollback.

To do that
* Your event class must override isTransactional() and return true
* Every eventlistener should implement TransactionalEventListener

```java
public class TransactionalEvent extends Event{
    @Override
    public boolean isTransactional() {
        return true;
    }
}
```

```java
public class EventListener1 implements TransactionalEventListener{
    @EventListener
    public void on(TransactionalEvent event){
        
    }
    
    @Override
    public void rollback(){
        //This will be executed if EventListener2 or EventListener1 fails
    }
}
```
```java
public class EventListener2 implements TransactionalEventListener{
    @EventListener
    public void on(TransactionalEvent event){
        
    }
    
    @Override
    public void rollback(){
        //This will be executed
    }
}
```

### Priority

An event listener can have a priority. This priority will determine the order that it will get executed.

```java
@EventListener(priority = Priority.LOWEST)
public void on(A event) {
    //It will be executed the last one
}

@EventListener(priority = Priority.HIGHEST)
public void on(A event) {
    //It will be executed the first one
}
```
### Listen to certain types of events

An event listener can listen an event that needs to implement a certain (or various) interface.

Consider A implements DBTransaction interface

```java
@EventListener({DBTransaction.class})
public void on(A event) {
    //It will listen to all events of type and subtype of A that implements DBTransaction interface
}
```
