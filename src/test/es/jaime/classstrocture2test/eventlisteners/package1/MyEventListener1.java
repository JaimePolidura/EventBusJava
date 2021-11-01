package es.jaime.classstrocture2test.eventlisteners.package1;

import es.jaime.EventListener;
import es.jaime.Priority;
import es.jaime.classstrocture2test.MyEvent;
import es.jaime.classstrocture2test.MyEventListener;

public final class MyEventListener1 extends MyEventListener {
    private boolean eventRaised;

    public MyEventListener1() {
        this.eventRaised = false;
    }

    @EventListener(pritority = Priority.LOW)
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
