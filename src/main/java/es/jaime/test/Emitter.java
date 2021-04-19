package es.jaime.test;

import es.jaime.EventBus;
import es.jaime.EventBusAsynch;
import es.jaime.EventListener;

import java.util.concurrent.Executors;

public final class Emitter {
    public static void main(String[] args) {
        EventBus bus = new EventBusAsynch(Executors.newSingleThreadExecutor(), "es.jaime");

        MyCustomEvent customEvent = new MyCustomEvent("customEvent");
        bus.publish(customEvent);

        MyCustomEvent2 customEvent2 = new MyCustomEvent2("customevent2", "juanpedro");
        bus.publish(customEvent2);
    }

    @EventListener
    public void on (MyCustomEvent customEvent) {
        System.out.println("1");
    }

    @EventListener
    public void on2 (MyCustomEvent customEvent) {
        System.out.println("2");
    }

    @EventListener
    public void on3 (MyCustomEvent2 customEvent2) {
        System.out.println("subtype");
    }
}
