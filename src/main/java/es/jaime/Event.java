package es.jaime;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

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

    public String getEventName(){
        return "";
    }

    public boolean isTransactional() {
        return false;
    }
}
