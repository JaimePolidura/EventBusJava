package es.jaime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EventListenerCache {
    private final Map<Class<? extends Event>, List<EventListenerInfo>> cachedEventListeners;

    public EventListenerCache () {
        this.cachedEventListeners = new HashMap<>();
    }

    public void put (Class<? extends Event> event, List<EventListenerInfo> listeners) {
        this.cachedEventListeners.put(event, listeners);
    }

    public boolean isCached (Class<? extends Event> event) {
        return this.cachedEventListeners.get(event) != null;
    }

    public List<EventListenerInfo> get (Class<? extends Event> event) {
        return this.cachedEventListeners.get(event);
    }
}
