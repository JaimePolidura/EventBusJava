package es.jaime;

import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

public final class EventConsumer {
    private final EventsListenersMapper mapper;
    private final EventListenerCache cache;

    public EventConsumer(EventsListenersMapper mapper) {
        this.mapper = mapper;
        this.cache = new EventListenerCache();
    }

    @SneakyThrows
    public void consume (Event event) {
        Class<? extends Event> classEvent = event.getClass();

        if(cache.isCached(classEvent)){
            executeWhatItIsInCache(event);
        }else {
            executeEventAndAddCache(event);
        }
    }

    @SneakyThrows
    private void executeWhatItIsInCache (Event event) {
        List<EventListenerInfo> eventListeners = cache.get(event.getClass());

        for (EventListenerInfo eventListener : eventListeners) {
            eventListener.method.invoke(eventListener.instance, event);
        }
    }

    private void executeEventAndAddCache(Event event) {
        List<EventListenerInfo> listenersToAddInCache = new LinkedList<>();
        Class<? extends Event> classEventToCheck = event.getClass();
        Set<Class<?>> interfacesAccumulator = new HashSet<>();

        //class event not null && class not equals objecy (we wanna iterate over all events super classes)
        while (classEventToCheck != null && !classEventToCheck.equals(Object.class)) {
            List<EventListenerInfo> eventListeners = mapper.searchEventListeners(classEventToCheck);

            interfacesAccumulator.addAll(Arrays.asList(classEventToCheck.getInterfaces()));

            if(eventListeners == null || eventListeners.isEmpty()){
                classEventToCheck = (Class<? extends Event>) classEventToCheck.getSuperclass();
                continue;
            }

            executeEventListeners(event, eventListeners, interfacesAccumulator, listenersToAddInCache);

            classEventToCheck = (Class<? extends Event>) classEventToCheck.getSuperclass();
        }

        cache.put(classEventToCheck, listenersToAddInCache);
    }

    @SneakyThrows
    private void executeEventListeners (Event event, List<EventListenerInfo> eventListenerInfos, Set<Class<?>> interfacesAccumulator,
                                        List<EventListenerInfo> listenersToAddInCache) {
        for (EventListenerInfo eventListenerInfo : eventListenerInfos) {
            Object instance = eventListenerInfo.instance;
            Method method = eventListenerInfo.method;
            Class<?>[] interfaces = eventListenerInfo.eventListenerAnnotation.value();

            if(notInterfacesNeeded(interfaces) || containsNeededInterfaces(interfaces, interfacesAccumulator)){
                method.invoke(instance, event);

                listenersToAddInCache.add(eventListenerInfo);
            }
        }
    }

    private boolean notInterfacesNeeded (Class<?>[] interfacesToImplement) {
        return interfacesToImplement == null || interfacesToImplement.length == 0;
    }

    private boolean containsNeededInterfaces(Class<?>[] interfacesToImplement, Set<Class<?>> interfaceAccumulator) {
        return Stream.of(interfacesToImplement).anyMatch(interfaceAccumulator::contains);
    }
}
