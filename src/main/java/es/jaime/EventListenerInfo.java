package es.jaime;

import lombok.AllArgsConstructor;

import java.lang.reflect.Method;

@AllArgsConstructor
public final class EventListenerInfo {
    public final Object instance;
    public final Method method;
    public final Class<?>[] interfacesNeedToImplement;

    public static EventListenerInfo of (Object instance, Method method, Class<?>[] interfacesNeedToImplement) {
        return new EventListenerInfo(instance, method, interfacesNeedToImplement);
    }
}
