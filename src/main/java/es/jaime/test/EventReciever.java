package es.jaime.test;

import es.jaime.EventListener;

public final class EventReciever {
    @EventListener
    public void on (MyCustomEvent customEvent) {
        System.out.println("3");
    }
}
