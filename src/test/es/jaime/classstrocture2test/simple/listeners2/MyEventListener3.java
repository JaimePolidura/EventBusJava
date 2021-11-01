package es.jaime.classstrocture2test.simple.listeners2;

import es.jaime.EventListener;
import es.jaime.Priority;
import es.jaime.classstrocture2test.simple.MyEvent;
import es.jaime.classstrocture2test.MyEventListener;

public final class MyEventListener3 extends MyEventListener {
    private boolean eventRaised;

    @EventListener(pritority = Priority.HIGH)
    public void onMyEvent(MyEvent myEvent) {
        this.eventRaised = true;
    }

    @Override
    public boolean isEventRaised() {
        boolean toReturn = eventRaised;
        this.eventRaised = false;

        return toReturn;
    }
}
