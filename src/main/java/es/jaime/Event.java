package es.jaime;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * To create an event it should extends this class
 */
public abstract class Event implements Comparable<Event> {
    @Getter private final LocalDateTime timeOnCreated;
    @Getter private final UUID eventUuid;

    public Event () {
        this.timeOnCreated = LocalDateTime.now();
        this.eventUuid = UUID.randomUUID();
    }

    @Override
    public int compareTo(Event o) {
        return this.timeOnCreated.compareTo(o.getTimeOnCreated());
    }

    /**
     * This method will return the eventname, it can be overridable by a subclass
     * @return event name
     */
    public String getEventName(){
        return "";
    }

    /**
     * This method will return if the event is transactional or not.
     * If it return true, it means that when an event listener fails, the event manager will call to every eventlistener to rollback
     * Every event listener should implement TransactionalEventListener
     * @return isTransactional
     */
    public boolean isTransactional() {
        return false;
    }
}
