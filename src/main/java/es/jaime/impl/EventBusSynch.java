package es.jaime.impl;

import es.jaime.Event;
import es.jaime.EventBus;
import es.jaime.EventsListenersMapper;
import es.jaime.EventConsumer;

import lombok.NonNull;

import java.util.Collection;

public final class EventBusSynch implements EventBus {
    private final EventsListenersMapper eventsListenersMapper;
    private final EventConsumer eventConsumer;

    public EventBusSynch (String packageToScan) {
        this.eventsListenersMapper = new EventsListenersMapper(packageToScan);
        this.eventConsumer = new EventConsumer(eventsListenersMapper);
    }

    @Override
    public void publish (@NonNull Collection<? extends Event> events) {
        events.forEach(this.eventConsumer::consume);
    }

    @Override
    public void publish (@NonNull Event event) {
        this.eventConsumer.consume(event);
    }
}
