package es.jaime.classstrocture2test.simple.listeners1;

import es.jaime.EventListener;
import es.jaime.classstrocture2test.simple.MyEvent;
import es.jaime.classstrocture2test.MyEventListener;

public final class MyEventListener2 extends MyEventListener {
    private boolean eventRaised;

    @EventListener
    public void onMyEvent1(MyEvent myEvent) {
        this.eventRaised = true;
    }

    @Override
    public boolean isEventRaised() {
        boolean toReturn = eventRaised;
        this.eventRaised = false;

        return toReturn;
    }
}
