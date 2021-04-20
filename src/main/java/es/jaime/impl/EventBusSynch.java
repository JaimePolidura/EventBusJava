package es.jaime.impl;

import es.jaime.Event;
import es.jaime.EventBus;
import es.jaime.EventListenerInfo;
import es.jaime.EventsListenersMapper;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

public final class EventBusSynch implements EventBus {
    private final EventsListenersMapper eventsListenersMapper;

    public EventBusSynch (String packageToScan) {
        this.eventsListenersMapper = new EventsListenersMapper(packageToScan);
    }

    @Override
    public synchronized void publish (@NonNull Collection<? extends Event> events) {
        events.forEach(this::consumeEvent);
    }

    @Override
    public synchronized void publish (@NonNull Event event) {
        this.consumeEvent(event);
    }

    @SneakyThrows
    private void consumeEvent (Event event) {
        Class<? extends Event> classEventCheck = event.getClass();

        while (classEventCheck != null) {
            Set<EventListenerInfo> info = eventsListenersMapper.searchEventListeners(classEventCheck);

            if(info == null || info.isEmpty()){
                classEventCheck = (Class<? extends Event>) classEventCheck.getSuperclass();
                continue;
            }

            //Checking for event superclasses event listener
            for (EventListenerInfo eventListenerInfo : info) {
                Object instance = eventListenerInfo.instance;
                Method method = eventListenerInfo.method;

                method.invoke(instance, event);
            }

            classEventCheck = (Class<? extends Event>) classEventCheck.getSuperclass();
        }
    }
}
