package prueba;

import es.jaime.EventListener;

public class PlayerKilledEventListener {
    @EventListener
    public void onPlayerKilled (PlayerKillledEvent event) {
        System.out.println(event.getName());
    }
}
