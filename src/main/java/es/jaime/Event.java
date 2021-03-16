package es.jaime;

import java.time.LocalDateTime;
import java.util.UUID;

public interface Event extends Comparable<Event> {
    UUID getId();
    LocalDateTime getTimeOnCreated();

    @Override
    default int compareTo(Event otherEvent) {
        return this.getTimeOnCreated().compareTo(otherEvent.getTimeOnCreated());
    }
}
