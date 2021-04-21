package es.jaime.impl;

import es.jaime.*;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.Executor;

public final class EventBusAsynch implements Runnable, EventBus {
    private final Queue<Event> eventQueue;
    private final EventsListenersMapper eventsListenersMapper;
    private final EventConsumer eventConsumer;

    public EventBusAsynch(Executor executor, String packageToScan) {
        this.eventsListenersMapper = new EventsListenersMapper(packageToScan);
        this.eventQueue = new PriorityQueue<>();
        this.eventConsumer = new EventConsumer(eventsListenersMapper);

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

            if (event != null) {
                this.eventConsumer.consume(event);
            }

            Thread.sleep(1);
        }
    }
}
