package prueba;

import es.jaime.EventBus;

public class EventEmitter {
    public static void main(String[] args) {
        EventBus eventBus = new EventBus();

        PlayerKillledEvent playerKillledEvent = new PlayerKillledEvent("Yo");

        eventBus.publish(playerKillledEvent);
    }
}
