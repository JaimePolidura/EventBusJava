package es.jaime;

import com.sun.istack.internal.NotNull;

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

    public synchronized void publish (@NotNull Collection<? extends Event> messages) {
        messages.forEach(this::publish);
    }

    public synchronized void publish (@NotNull Event message) {
        this.events.add(message);
    }

    @Override
    public void run() {
        for(;;) {
            while (events.isEmpty());

            consumeEvent(events.poll());
        }
    }

    private void consumeEvent (Event event) {
        Set<Method> listeners = eventsListenersMapper.searchEventListeners(event.getClass());

        listeners.forEach(listener -> {
            try {
                listener.invoke(event);
            } catch (IllegalAccessException | InvocationTargetException e) {
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