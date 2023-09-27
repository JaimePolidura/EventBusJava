package es.jaime.impl;

import es.jaime.*;

import lombok.NonNull;

import java.util.Collection;

public final class EventBusSync implements EventBus {
    private final EventConsumer eventConsumer;
    private boolean alreadyScanned;

    public EventBusSync(String commonPackage, EventListenerDependencyProvider eventListenerDependencyProvider) {
        this.eventConsumer = new EventConsumer(eventListenerDependencyProvider, commonPackage);
        this.alreadyScanned = false;
    }

    public EventBusSync(String commonPackage) {
        this.eventConsumer = new EventConsumer(commonPackage);
        this.alreadyScanned = false;
    }

    @Override
    public void publish (@NonNull Collection<? extends Event> events) {
        scanForListeners();
        events.forEach(this.eventConsumer::consume);
    }

    @Override
    public void publish (@NonNull Event event) {
        scanForListeners();
        this.eventConsumer.consume(event);
    }

    public void scanForListeners() {
        if(!alreadyScanned){
            eventConsumer.scanForListeners();
            alreadyScanned = true;
        }
    }
}
