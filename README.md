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

```java
EventBus eventbus = new EventBusSynch("es.jaimetruman");

eventbus.publish(new GameTimedOutEvent("team 1", "team 2"));
```

## LISTENERS

```java
@EventListener
public void on(GameTimedOutEvent event) {
    System.out.printf("Game has ended beacause of time out %s %s", event.getTeam1(), event.getTeam2());
}
```
### Listen to main class

A extends Event

B extends A

```java
eventbus.publish(new A());
eventbus.publish(new B());
```

```java
@EventListener
public void on(A event) {
    //It will listen all events of type and subtype of A
}
```

### Priority

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

A implements DBTransaction interface

```java
@EventListener({DBTransaction.class})
public void on(A event) {
    //It will listen to all events of type and subtype of A that implements DBTransaction interface
}
```
