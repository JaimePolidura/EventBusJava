package es.jaime;

public interface EventListenerDependencyProvider {
    <I, O extends I> O get(Class<I> input);
}
