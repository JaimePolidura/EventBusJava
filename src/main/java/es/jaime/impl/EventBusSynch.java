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
    private final String packageToScan;

    public EventBusSynch (String packageToScan) {
        this.eventsListenersMapper = new EventsListenersMapper();
        this.eventConsumer = new EventConsumer(eventsListenersMapper);
        this.packageToScan = packageToScan;
    }

    @Override
    public synchronized void publish (@NonNull Collection<? extends Event> events) {
        events.forEach(this.eventConsumer::consume);
    }

    @Override
    public synchronized void publish (@NonNull Event event) {
        //If it has already been scanned it wont scan it
        this.eventsListenersMapper.scan(packageToScan);

        this.eventConsumer.consume(event);
    }
}
