# EventBusJava

This library provides a way to publish your own events and subscribe to them in a easy way.

## SET UP

## EVENT

Each event will be represented by a class. To create you own event you have to extend Event. Event class will only have the attribute of time on created. 

```java
@AllArgsConstructor
public class GameTimedOutEvent extends Event {
    @Getter private final String team1;
    @Getter private final String tema2;
}
```

## PUBLISHER

To publish a event you have two main ways already implemented by the library. 

* EventBusSynch. When a event is published, its subscriber will be executed in the same thread.
* EventBusAsynch. When a event is published it will be enqueued in a priority queue. There will be other thread that will be reading the queue, when an event is published it will execute its subscribers.

These both types implements EventBus interface

```java
EventBus synchEventBus = new EventBusSynch("es.jaimetruman");

synchEventBus.publish(new GameTimedOutEvent("team 1", "team 2"));
```

To instanciate a AysnchEvent bus you have to pass the thread pool provided by Executors.

```java
EventBus asynchEventBus = new EventBusAsynch(Executors.newSingleThreadExecutor(), "myPackag");

asynchEventBus.publish(new GameTimedOutEvent("team 1", "team 2"));
```

## LISTENERS

Each event listener will be represented by a method. To make this possible, the methods needs:

* Be annotated with @EventListener
* Have only one param, the Event class

!Important! The class that has the method needs to have at least one public empty constructor. The method name doesnt matter.

```java
@EventListener
public void on(GameTimedOutEvent event) {
    System.out.printf("Game has ended beacause of time out %s %s", event.getTeam1(), event.getTeam2());
}
```
### Listen to main class

If you want to listen a main class event. You can simply put the main class name in the mehtods para. Ex

Consider this case: A extends Event and B extends A. 

```java
@EventListener
public void on(A event) {
    //It will listen all events of type and subtype of A
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
### Listen to interfaces

An event listener can listen an event that needs to implement a certain (or various) interface.

Consider A implements DBTransaction interface

```java
@EventListener({DBTransaction.class})
public void on(A event) {
    //It will listen all events of type and subtype of A that implements DBTransaction interfae
}
```
