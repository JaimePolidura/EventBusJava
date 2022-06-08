package es.jaime;

import es.jaime.classstrocture2test.transactional.MyEventListenerTransactional;
import es.jaime.classstrocture2test.transactional.fail.MyEventTransactionalFail;
import es.jaime.classstrocture2test.transactional.success.MyEventTransactionalSuccess;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public final class EventConsumerTransactionalTest {
    private EventConsumer eventConsumer;
    private List<MyEventListenerTransactional> eventListenersFail;
    private List<MyEventListenerTransactional> eventListenersSuccess;

    @Before
    public void setUp() {
        EventsListenersMapper mapper = new EventsListenersMapper();

        List<MyEventListenerTransactional> eventListenersFail = new ArrayList<>();
        for (EventListenerInfo eventListenerInfo : mapper.searchEventListeners(MyEventTransactionalFail.class)) {
            eventListenersFail.add((MyEventListenerTransactional) eventListenerInfo.instance);
        }
        this.eventListenersFail = eventListenersFail;

        List<MyEventListenerTransactional> eventListenersSuccess = new ArrayList<>();
        for (EventListenerInfo eventListenerInfo : mapper.searchEventListeners(MyEventTransactionalSuccess.class)) {
            eventListenersSuccess.add((MyEventListenerTransactional) eventListenerInfo.instance);
        }
        this.eventListenersSuccess = eventListenersSuccess;

        this.eventConsumer = new EventConsumer(mapper);
    }

    @Test
    public void testRollback() {
        this.eventConsumer.consume(new MyEventTransactionalFail());

        assertEquals(0, eventListenersFail.get(0).getCounter());
        assertEquals(0, eventListenersFail.get(1).getCounter());

        //Test for cache
        this.eventConsumer.consume(new MyEventTransactionalFail());
        assertEquals(0, eventListenersFail.get(0).getCounter());
        assertEquals(0, eventListenersFail.get(1).getCounter());
    }

    @Test
    public void testSuccess() {
        this.eventConsumer.consume(new MyEventTransactionalSuccess());

        assertEquals(1, eventListenersSuccess.get(0).getCounter());
        assertEquals(1, eventListenersSuccess.get(1).getCounter());

        this.eventConsumer.consume(new MyEventTransactionalSuccess());
        assertEquals(2, eventListenersSuccess.get(0).getCounter());
        assertEquals(2, eventListenersSuccess.get(1).getCounter());
    }
}
