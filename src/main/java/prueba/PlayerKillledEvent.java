package prueba;

import es.jaime.Event;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerKillledEvent implements Event {
    private final UUID uuid;
    private final LocalDateTime createdTime;
    private final String name;

    public PlayerKillledEvent(String name) {
        this.name = name;
        this.createdTime = LocalDateTime.now();
        this.uuid = UUID.randomUUID();
    }

    @Override
    public UUID getId() {
        return uuid;
    }

    @Override
    public LocalDateTime getTimeOnCreated() {
        return createdTime;
    }

    public String getName() {
        return name;
    }
}
