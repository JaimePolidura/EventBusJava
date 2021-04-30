package es.jaime;

import lombok.AllArgsConstructor;

import java.lang.reflect.Method;

@AllArgsConstructor
public final class EventListenerInfo implements Comparable<EventListenerInfo>{
    public final Object instance;
    public final Method method;
    public final Class<?>[] interfacesNeedToImplement;
    public final Priority priority;

    public static EventListenerInfo of (Object instance, Method method, Class<?>[] interfacesNeedToImplement, Priority priority) {
        return new EventListenerInfo(instance, method, interfacesNeedToImplement, priority);
    }

    @Override
    public int compareTo(EventListenerInfo o) {
        return Integer.compare(o.priority.value, this.priority.value);
    }
}
