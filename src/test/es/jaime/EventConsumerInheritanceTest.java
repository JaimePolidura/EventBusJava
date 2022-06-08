package es.jaime;

import es.jaime.classstrocture2test.MyEventListener;
import es.jaime.classstrocture2test.inheritance.MyEventSubClass;
import es.jaime.classstrocture2test.inheritance.MyEventSuperClass;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class EventConsumerInheritanceTest {
    private EventConsumer eventConsumer;
    private List<MyEventListener> eventListenersSuper;
    private List<MyEventListener> eventListenersSub;

    @Before
    public void setUp() {
        EventsListenersMapper mapper = new EventsListenersMapper();

        List<MyEventListener> eventListenersSup = new ArrayList<>();
        for (EventListenerInfo eventListenerInfo : mapper.searchEventListeners(MyEventSuperClass.class)) {
            eventListenersSup.add((MyEventListener) eventListenerInfo.instance);
        }
        this.eventListenersSuper = eventListenersSup;

        List<MyEventListener> eventListenersSub = new ArrayList<>();
        for (EventListenerInfo eventListenerInfo : mapper.searchEventListeners(MyEventSubClass.class)) {
            eventListenersSub.add((MyEventListener) eventListenerInfo.instance);
        }
        this.eventListenersSub = eventListenersSub;

        this.eventConsumer = new EventConsumer(mapper);
    }

    @Test
    public void checkAllEventsRaisedSuper() {
        publishEventSuper();

        assertTrue(checkAllEventsSuperRaised());
    }

    @Test
    public void checkAllEventsRaisedSub() {
        publishEventSub();

        assertTrue(checkAllEventsSubRaised());
    }

    @Test
    public void checkTimesEventSuperRaised() {
        publishEventSuper();

        assertEquals(1, eventListenersSuper.get(0).getTimesRaised());
    }

    @Test
    public void checkTimesEventSubRaised() {
        publishEventSub();

        int totalTimesRaised = eventListenersSub.get(0).getTimesRaised() + eventListenersSuper.get(0).getTimesRaised();

        assertEquals(2, totalTimesRaised);
    }

    private void publishEventSuper() {
        eventConsumer.consume(new MyEventSuperClass());
    }

    private void publishEventSub() {
        eventConsumer.consume(new MyEventSubClass());
    }

    private boolean checkAllEventsSuperRaised(){
        return eventListenersSuper.stream().allMatch(MyEventListener::isEventRaised);
    }

    private boolean checkAllEventsSubRaised(){
        return eventListenersSub.stream().allMatch(MyEventListener::isEventRaised);
    }
}
