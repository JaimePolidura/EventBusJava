package es.jaime;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

public interface EventBus {
    void publish (@NonNull Collection<? extends Event> events);
    void publish (@NonNull Event event);
}
