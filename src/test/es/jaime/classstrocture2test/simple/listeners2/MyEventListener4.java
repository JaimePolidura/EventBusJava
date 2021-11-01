package es.jaime.classstrocture2test.simple.listeners2;

import es.jaime.EventListener;
import es.jaime.Priority;
import es.jaime.classstrocture2test.simple.MyEvent;
import es.jaime.classstrocture2test.MyEventListener;
import es.jaime.classstrocture2test.simple.MyInterface;

public final class MyEventListener4 extends MyEventListener {
    private boolean eventRaised;

    @EventListener(pritority = Priority.HIGHEST, value = MyInterface.class)
    public void onMyEvent2(MyEvent myEvent) {
        this.eventRaised = true;
    }

    @Override
    public boolean isEventRaised() {
        boolean toReturn = eventRaised;
        this.eventRaised = false;

        return toReturn;
    }
}
