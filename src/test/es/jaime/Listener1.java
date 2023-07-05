package es.jaime;

public final class Listener1 {
    @EventListener
    public void on(Evento evento) {
        System.out.println("1");
    }
}
