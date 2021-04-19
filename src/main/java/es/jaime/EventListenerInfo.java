package es.jaime;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

@AllArgsConstructor
public final class EventListenerInfo {
    public final Object instance;
    public final Method method;

    public static EventListenerInfo of (Object instance, Method method) {
        return new EventListenerInfo(instance, method);
    }
}
