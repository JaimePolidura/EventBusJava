package es.jaime;

import es.jaime.classstrocture2test.MyEvent;
import es.jaime.classstrocture2test.MyEventListener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public final class EventConsumerTest {
    private EventConsumer eventConsumer;
    private List<MyEventListener> eventListeners;

    @Before
    public void setUp() {
        EventsListenersMapper mapper = new EventsListenersMapper("es.jaime.classstrocture2test.eventlisteners");

        List<MyEventListener> eventListeners = new ArrayList<>();
        for (EventListenerInfo eventListenerInfo : mapper.searchEventListeners(MyEvent.class)) {
            eventListeners.add((MyEventListener) eventListenerInfo.instance);
        }
        this.eventListeners = eventListeners;

        this.eventConsumer = new EventConsumer(mapper);
    }

    @Test
    public void checkFirstEventRaised() {
        publishEvent();

        assertTrue(checkAllEventsRaised());
    }

    @Test
    public void checkSecondEventRaised() {
        publishEvent();

        assertTrue(checkAllEventsRaised());
    }

    private void publishEvent() {
        eventConsumer.consume(new MyEvent());
    }

    private boolean checkAllEventsRaised(){
        return eventListeners.stream().allMatch(MyEventListener::isEventRaised);
    }
}
