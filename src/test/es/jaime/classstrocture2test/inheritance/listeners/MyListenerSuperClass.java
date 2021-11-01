package es.jaime.classstrocture2test.inheritance.listeners;

import es.jaime.EventListener;
import es.jaime.classstrocture2test.MyEventListener;
import es.jaime.classstrocture2test.inheritance.MyEventSuperClass;

public final class MyListenerSuperClass extends MyEventListener {
    private boolean eventRaised;

    @EventListener
    public void on (MyEventSuperClass event) {
        this.eventRaised = true;

        incrementTimesRaised();
    }

    @Override
    public boolean isEventRaised() {
        boolean toReturn = this.eventRaised;
        this.eventRaised = false;

        return toReturn;
    }
}
