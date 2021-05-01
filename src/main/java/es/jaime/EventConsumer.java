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

    @SneakyThrows
    private void executeEventAndAddCache(Event event) {
        List<EventListenerInfo> listenersToAddInCache = new LinkedList<>();

        Class<? extends Event> classEvent = event.getClass();
        Set<Class<?>> interfacesAccumulator = new HashSet<>();

        while (classEvent != null && !classEvent.equals(Object.class)) {
            List<EventListenerInfo> info = mapper.searchEventListeners(classEvent);

            interfacesAccumulator.addAll(Arrays.asList(classEvent.getInterfaces()));

            if(info == null || info.isEmpty()){
                classEvent = (Class<? extends Event>) classEvent.getSuperclass();
                continue;
            }

            //Checking for event superclasses event listener
            for (EventListenerInfo eventListenerInfo : info) {
                Object instance = eventListenerInfo.instance;
                Method method = eventListenerInfo.method;
                Class<?>[] interfaces = eventListenerInfo.eventListenerAnnotation.value();

                if(checkIfContainsInterface(interfaces, interfacesAccumulator)){
                    method.invoke(instance, event);

                    listenersToAddInCache.add(eventListenerInfo);
                }

            }

            classEvent = (Class<? extends Event>) classEvent.getSuperclass();
        }
    }

    private boolean checkIfContainsInterface (Class<?>[] interfacesToImplement, Set<Class<?>> interfaceAccumulator) {
        if(interfacesToImplement == null || interfacesToImplement.length == 0){
            return true; //No interfaces
        }

        return Stream.of(interfacesToImplement)
                .anyMatch(interfaceAccumulator::contains);
    }
}
