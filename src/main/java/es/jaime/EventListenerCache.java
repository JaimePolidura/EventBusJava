package es.jaime;

import java.util.*;

public final class EventListenerCache {
    private final Map<Class<? extends Event>, List<EventListenerInfo>> cachedEventListeners;

    public EventListenerCache () {
        this.cachedEventListeners = new HashMap<>();
    }

    public void put (Class<? extends Event> event, EventListenerInfo listener) {
        mergeListToMap(event, listener);
    }

    private void mergeListToMap (Class<? extends Event> event, EventListenerInfo listener) {
        List<EventListenerInfo> eventListeners = cachedEventListeners.get(event);

        if(eventListeners == null){
            cachedEventListeners.put(event, listOf(listener));
        }else{
            List<EventListenerInfo> eventListenerInfo = cachedEventListeners.get(event);
            eventListenerInfo.add(listener);

            cachedEventListeners.put(event, eventListenerInfo);
        }
    }

    private <E> List<E> listOf (E... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

    public boolean isCached (Class<? extends Event> event) {
        return this.cachedEventListeners.get(event) != null;
    }

    public List<EventListenerInfo> get (Class<? extends Event> event) {
        return this.cachedEventListeners.get(event);
    }
}
