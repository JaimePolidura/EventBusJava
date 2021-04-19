package es.jaime;

import lombok.NonNull;

import java.util.Collection;

public interface EventBus {
    void publish (@NonNull Collection<? extends Event> events);
    void publish (@NonNull Event event);
}
