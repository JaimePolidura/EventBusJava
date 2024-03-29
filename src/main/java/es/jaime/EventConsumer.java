package es.jaime;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

public final class EventConsumer {
    private final SimpleEventListenersMapper mapper;
    private final EventListenerCache cache;
    private final String commonPackage;

    public EventConsumer(String commonPackage) {
        this.mapper = new SimpleEventListenersMapper(commonPackage);
        this.cache = new EventListenerCache();
        this.commonPackage = commonPackage;
    }

    public EventConsumer(EventListenerDependencyProvider eventListenerDependencyProvider, String commonPackage) {
        this.mapper = new SimpleEventListenersMapper(eventListenerDependencyProvider, commonPackage);
        this.cache = new EventListenerCache();
        this.commonPackage = commonPackage;
    }

    public void scanForListeners() {
        this.mapper.scanForListeners();
    }

    public void consume (Event event) {
        Class<? extends Event> classEvent = event.getClass();

        if(cache.isCached(classEvent)){
            executeWhatItIsInCache(event);
        }else {
            executeEventAndAddCache(event);
        }
    }

    private void executeWhatItIsInCache (Event event) {
        List<EventListenerInfo> eventListeners = cache.get(event.getClass());

        for (EventListenerInfo actualEventListenerInfo : eventListeners) {
            Method method = actualEventListenerInfo.method;
            Object instance = actualEventListenerInfo.instance;

            executeEventListener(method, instance, event);
        }
    }

    private void executeEventAndAddCache(Event event) {
        Class<? extends Event> classEventToCheck = event.getClass();
        Set<Class<?>> interfacesAccumulator = new HashSet<>();

        //Iterate to all event superclasses to find listeners
        while (classEventToCheck != null && !classEventToCheck.equals(Object.class)) {
            List<Class<?>> interfacesClassEventToCheck = Arrays.asList(classEventToCheck.getInterfaces());
            List<EventListenerInfo> eventListenersByEventClass = mapper.findEventListenerInfoByEvent(classEventToCheck);

            interfacesAccumulator.addAll(interfacesClassEventToCheck);

            if(eventListenersByEventClass != null){
                executeEventListeners(event, eventListenersByEventClass, interfacesAccumulator);
            }
            if(!interfacesClassEventToCheck.isEmpty()){
                executeEventListenersInterfaces(event, classEventToCheck, interfacesClassEventToCheck);
            }

            classEventToCheck = (Class<? extends Event>) classEventToCheck.getSuperclass();
        }
    }

    private void executeEventListeners (Event event, List<EventListenerInfo> eventListenerInfos, Set<Class<?>> interfacesAccumulator) {
        for (EventListenerInfo eventListenerInfo : eventListenerInfos) {
            Object instance = eventListenerInfo.instance;
            Method method = eventListenerInfo.method;
            Class<?>[] interfaces = eventListenerInfo.eventListenerAnnotation.value();

            if (notInterfacesNeeded(interfaces) || containsNeededInterfaces(interfaces, interfacesAccumulator)) {
                executeEventListener(method, instance, event);

                cache.put(event.getClass(), eventListenerInfo);
            }
        }
    }

    private void executeEventListenersInterfaces(Event instanceEvent, Class<? extends Event> clazzEvent, List<Class<?>> interfaces) {
        for (Class<?> interfacee : interfaces) {
            if(!Event.class.isAssignableFrom(interfacee) || interfacee.equals(Event.class)){
                continue;
            }

            for (EventListenerInfo eventListenerInfo : mapper.findEventListenerInfoByEvent((Class<? extends Event>) interfacee)) {
                executeEventListener(eventListenerInfo.method, eventListenerInfo.instance, instanceEvent);

                cache.put(clazzEvent, eventListenerInfo);
                cache.put((Class<? extends Event>) interfacee, eventListenerInfo);
            }
        }
    }

    private void executeEventListener(Method method, Object eventListenerInstance, Event event) {
        try {
            method.invoke(eventListenerInstance, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean notInterfacesNeeded (Class<?>[] interfacesToImplement) {
        return interfacesToImplement == null || interfacesToImplement.length == 0;
    }

    private boolean containsNeededInterfaces(Class<?>[] interfacesToImplement, Set<Class<?>> interfaceAccumulator) {
        return Stream.of(interfacesToImplement).anyMatch(interfaceAccumulator::contains);
    }
}
