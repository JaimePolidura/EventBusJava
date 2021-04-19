package es.jaime;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executor;

public final class EventBusAsynch implements Runnable, EventBus {
    private final Queue<Event> eventQueue;
    private final EventsListenersMapper eventsListenersMapper;

    public EventBusAsynch(Executor executor) {
        this.eventsListenersMapper = new EventsListenersMapper();
        this.eventQueue = new PriorityQueue<>();

        executor.execute(this);
    }

    @Override
    public synchronized void publish (@NonNull Collection<? extends Event> messages) {
        messages.forEach(this::publish);
    }

    @Override
    public synchronized void publish (@NonNull Event message) {
        this.eventQueue.add(message);
    }

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            Event event = this.eventQueue.poll();

            if(event != null){
                consumeEvent(event);
            }

            Thread.sleep(1);
        }
    }

    @SneakyThrows
    private void consumeEvent (Event event) {
        Set<EventListenerInfo> listeners = eventsListenersMapper.searchEventListeners(event.getClass());

        for (EventListenerInfo eventListenerInfo : listeners) {
            Object instance = eventListenerInfo.instance;
            Method method = eventListenerInfo.method;

            method.invoke(instance, event);
        }
    }
}
