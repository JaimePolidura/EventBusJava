package es.jaime.classstrocture2test.transactional;

public abstract class MyEventListenerTransactional {
    protected int counter;

    public void incrementCounter() {
        counter++;
    }

    public void decrementCounter() {
        counter--;
    }

    public int getCounter() {
        return counter;
    }
}
