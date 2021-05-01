package es.jaime;

import lombok.AllArgsConstructor;

import java.lang.reflect.Method;

@AllArgsConstructor
public final class EventListenerInfo implements Comparable<EventListenerInfo>{
    public final Object instance;
    public final Method method;
    public final EventListener eventListenerAnnotation;

    public static EventListenerInfo of (Object instance, Method method, EventListener eventListenerAnnotation) {
        return new EventListenerInfo(instance, method, eventListenerAnnotation);
    }

    @Override
    public int compareTo(EventListenerInfo o) {
        return Integer.compare(o.eventListenerAnnotation.pritority().value,
                this.eventListenerAnnotation.pritority().value);
    }
}
