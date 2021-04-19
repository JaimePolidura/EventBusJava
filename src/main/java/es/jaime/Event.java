package es.jaime;

import lombok.Getter;

import java.time.LocalDateTime;

public abstract class Event implements Comparable<Event> {
    @Getter private final LocalDateTime timeOnCreated;

    public Event () {
        this.timeOnCreated = LocalDateTime.now();
    }

    @Override
    public int compareTo(Event o) {
        return this.timeOnCreated.compareTo(o.getTimeOnCreated());
    }
}
