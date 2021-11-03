package es.jaime.classstrocture2test;

import es.jaime.classstrocture2test.transactional.MyEventListenerTransactional;

public abstract class MyEventListener extends MyEventListenerTransactional {
    private int timesRaised;

    public void incrementTimesRaised() {
        this.timesRaised++;
    }

    public int getTimesRaised() {
        return timesRaised;
    }

    public abstract boolean isEventRaised();
}
