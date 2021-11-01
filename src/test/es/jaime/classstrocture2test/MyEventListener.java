package es.jaime.classstrocture2test;

public abstract class MyEventListener {
    private int timesRaised;

    public void incrementTimesRaised() {
        this.timesRaised++;
    }

    public int getTimesRaised() {
        return timesRaised;
    }

    public abstract boolean isEventRaised();
}
