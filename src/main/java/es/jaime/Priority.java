package es.jaime;

public enum Priority {
    LOWEST(1),
    LOW(2),
    NORMAL(3),
    HIGH(4),
    HIGHEST(5);

    public final int value;

    Priority(int priority) {
        this.value = priority;
    }
}
