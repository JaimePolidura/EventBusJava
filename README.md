# EventBusJava

This library provides a way to publish your own events and subscribe to them in a easy and powerful way.

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

* EventBusSynch. When a event is published, its subscriber will be executed in the same thread. This implements EventBus interface.

```java
EventBus eventbus = new EventBusSynch("es.jaimetruman");

eventbus.publish(new GameTimedOutEvent("team 1", "team 2"));
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

In this case: A extends Event and B extends A. 

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

If some of your eventlistener fails maybe you want the event manager to call to each event listener to rollback. 

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
    //It will listen all events of type and subtype of A that implements DBTransaction interfae
}
```
