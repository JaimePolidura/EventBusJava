package es.jaime;

import io.vavr.control.Try;
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
    public synchronized void consume (Event event) {
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
        Class<? extends Event> classEventToCheck = event.getClass();
        Set<Class<?>> interfacesAccumulator = new HashSet<>();
        
        //class event not null && class not equals objecy (we wanna iterate over all events super classes)
        while (classEventToCheck != null && !classEventToCheck.equals(Object.class)) {
            interfacesAccumulator.addAll(Arrays.asList(classEventToCheck.getInterfaces()));
            List<EventListenerInfo> eventListeners = mapper.searchEventListeners(classEventToCheck);

            if(eventListeners != null){
                executeEventListeners(event, eventListeners, interfacesAccumulator);
            }

            classEventToCheck = (Class<? extends Event>) classEventToCheck.getSuperclass();
        }
    }

    private void executeEventListeners (Event event, List<EventListenerInfo> eventListenerInfos, Set<Class<?>> interfacesAccumulator) {
        for(int i = 0; i < eventListenerInfos.size(); i++) {
            EventListenerInfo eventListenerInfo = eventListenerInfos.get(i);

            Object instance = eventListenerInfo.instance;
            Method method = eventListenerInfo.method;
            Class<?>[] interfaces = eventListenerInfo.eventListenerAnnotation.value();

            if(notInterfacesNeeded(interfaces) || containsNeededInterfaces(interfaces, interfacesAccumulator)){
                boolean success = executeEventListener(method, instance, event, eventListenerInfo);

                if(!success){
                    startRollBack(eventListenerInfos, i);
                    cache.remove(event.getClass());
                    return;
                }
            }
        }
    }

    private boolean executeEventListener(Method method, Object instance, Event event, EventListenerInfo eventListenerInfo) {
        Try<Void> tryMethod = Try.run(() -> {
            method.invoke(instance, event);
            cache.put(event.getClass(), eventListenerInfo);
        });

        return tryMethod.isSuccess();
    }

    private void startRollBack(List<EventListenerInfo> eventListeners, int eventListenerError) {
        for(int i = eventListenerError; i >= 0; i--){
            EventListenerInfo actualEventListener = eventListeners.get(i);
            Class<?> eventListenerClass = actualEventListener.instance.getClass();

            if(ReflectionsUtils.containsInterface(eventListenerClass, TransactionalEventListener.class)){
                TransactionalEventListener transactionalEvent = (TransactionalEventListener) actualEventListener.instance;
                transactionalEvent.rollback();
            }else{
                onTransactionalEventListenerNotImplemented();
            }
        }
    }

    private void onTransactionalEventListenerNotImplemented() {
        try{
            throw new TransacionalEventListenerNotImplemented("TransactionalEventListener interface should be implemented " +
                    "because the event is marked as transactional");
        }catch (TransacionalEventListenerNotImplemented e){
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
