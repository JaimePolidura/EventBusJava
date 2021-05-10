# EventBusJava

This library provides a way to publish your own events and subscribe to them in a easy way.

## SET UP

## EVENT

Each event will be represented by a class. To create you own event you have to extend Event. Event class will only have the attribute of time on created. 

```
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

```
EventBus synchEventBus = new EventBusSynch("es.jaimetruman");

synchEventBus.publish(new GameTimedOutEvent("team 1", "team 2"));
```

To instanciate a AysnchEvent bus you have to pass the thread pool provided by Executors.

```
EventBus asynchEventBus = new EventBusAsynch(Executors.newSingleThreadExecutor(), "myPackag");

asynchEventBus.publish(new GameTimedOutEvent("team 1", "team 2"));
```

## SUBSCRIBE
