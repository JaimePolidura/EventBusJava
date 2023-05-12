package es.jaime.impl;

import es.jaime.*;

import lombok.NonNull;

import java.util.Collection;

public final class EventBusSync implements EventBus {
    private final EventConsumer eventConsumer;
    private final String packageToScan;

    public EventBusSync(String commonPackage, EventListenerDependencyProvider eventListenerDependencyProvider) {
        this.eventConsumer = new EventConsumer(eventListenerDependencyProvider, commonPackage);
        this.packageToScan = commonPackage;
    }

    public EventBusSync(String commonPackage) {
        this.eventConsumer = new EventConsumer(commonPackage);
        this.packageToScan = commonPackage;
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
