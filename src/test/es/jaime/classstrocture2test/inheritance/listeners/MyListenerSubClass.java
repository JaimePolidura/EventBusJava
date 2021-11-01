package es.jaime.classstrocture2test.inheritance.listeners;

import es.jaime.EventListener;
import es.jaime.classstrocture2test.MyEventListener;
import es.jaime.classstrocture2test.inheritance.MyEventSubClass;

public final class MyListenerSubClass extends MyEventListener {
    private boolean eventRaised;

    @EventListener
    public void on (MyEventSubClass eventSubClass){
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
