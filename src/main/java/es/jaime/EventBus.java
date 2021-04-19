package es.jaime;

import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class EventBus implements Runnable {
    private final Queue<Event> events;
    private final EventsListenersMapper eventsListenersMapper;

    public EventBus() {
        this.eventsListenersMapper = new EventsListenersMapper();
        this.events = new PriorityQueue<>();
    }

    public synchronized void publish (Collection<? extends Event> messages) {
        Objects.requireNonNull(messages);

        messages.forEach(this::publish);
    }

    public synchronized void publish (Event message) {
        Objects.requireNonNull(message);

        this.events.add(message);
    }

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            Event event = events.poll();

            if(event != null){
                this.consumeEvent(event);
            }

            Thread.sleep(1000);
        }
    }

    private void consumeEvent (Event event) {
        Set<EventListenerInfo> listeners = eventsListenersMapper.searchEventListeners(event.getClass());

        listeners.forEach(listener -> {
            try {
                listener.method.invoke(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ack(event.getId());
    }

    private void ack (UUID uuid) {
        events.forEach(event -> {
            if(event.getId().equals(uuid))
                this.events.remove(event);
        });
    }
}