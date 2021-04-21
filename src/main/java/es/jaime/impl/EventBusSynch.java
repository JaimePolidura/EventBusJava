package es.jaime.impl;

import es.jaime.Event;
import es.jaime.EventBus;
import es.jaime.EventsListenersMapper;
import lombok.NonNull;

import java.util.Collection;

public final class EventBusSynch implements EventBus {
    private final EventsListenersMapper eventsListenersMapper;

    public EventBusSynch (String packageToScan) {
        this.eventsListenersMapper = new EventsListenersMapper(packageToScan);
    }

    @Override
    public void publish (@NonNull Collection<? extends Event> events) {
        events.forEach(event -> consumeEvent(event, eventsListenersMapper));
    }

    @Override
    public void publish (@NonNull Event event) {
        this.consumeEvent(event, eventsListenersMapper);
    }
}
