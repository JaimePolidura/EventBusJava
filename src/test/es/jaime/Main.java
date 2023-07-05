package es.jaime;

import es.jaime.impl.EventBusSync;

public final class Main {
    public static void main(String[] args) {
        EventBus eventBus = new EventBusSync("es.jaime");
        eventBus.publish(new Evento2());
        eventBus.publish(new Evento2());
        eventBus.publish(new Evento());
        eventBus.publish(new Evento());
        eventBus.publish(new Evento2());
    }
}
