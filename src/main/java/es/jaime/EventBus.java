package es.jaime;

import com.sun.istack.internal.NotNull;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executor;

public final class EventBus implements Runnable {
    private final Queue<Event> eventQueue;
    private final EventsListenersMapper eventsListenersMapper;
    private final Executor executor;

    public EventBus(Executor executor) {
        this.executor = executor;
        this.eventsListenersMapper = new EventsListenersMapper();
        this.eventQueue = new PriorityQueue<>();

        this.executor.execute(this);
    }

    public synchronized void publish (Collection<? extends Event> messages) {
        Objects.requireNonNull(messages);

        messages.forEach(this::publish);
    }

    public synchronized void publish (@NotNull Event message) {
        this.eventQueue.add(message);
    }

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            if(event != null){
                consumeEvent(event);
            }

            Thread.sleep(1000);
        }
    }

    @SneakyThrows
    private void consumeEvent (Event event) {
        Set<EventListenerInfo> listeners = eventsListenersMapper.searchEventListeners(event.getClass());

        for (Method listener : listeners) {
            listener.invoke(listener.getDeclaringClass().newInstance(), event);
        }

        ack(event.getId());
    }

    private void ack (UUID uuid) {
        eventQueue.forEach(event -> {
            if(event.getId().equals(uuid))
                this.eventQueue.remove(event);
        });
    }
}
