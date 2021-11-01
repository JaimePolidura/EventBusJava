package es.jaime;

import es.jaime.classstrocture2test.MyEvent;
import es.jaime.classstrocture2test.MyInterface;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public final class EventListenerMapperTest {
    private EventsListenersMapper eventListenerMapper;
    private List<EventListenerInfo> eventListenerInfosForMyEvent;

    @Before
    public void setUp() {
        this.eventListenerMapper = new EventsListenersMapper("es.jaime.classstrocture2test.eventlisteners");
        this.eventListenerInfosForMyEvent = eventListenerMapper.searchEventListeners(MyEvent.class);
    }

    @Test
    public void checkNumberOfListeners() {
        assertEquals(4, eventListenerInfosForMyEvent.size());
    }

    @Test
    public void checkPriorityOrder() {
        assertTrue(isSortedByPriorityOrder(eventListenerInfosForMyEvent));
    }

    @Test
    public void checkContainsInterfaceInfo() {
        //MyEventListener4.class highst priority -> first place
        EventListenerInfo eventListenerInfo = eventListenerInfosForMyEvent.get(0);

        assertEquals(MyInterface.class, eventListenerInfo.eventListenerAnnotation.value()[0]);
    }

    // Should be order in this way: 5 -> 4 -> 3 -> 2 -> 1
    private boolean isSortedByPriorityOrder(List<EventListenerInfo> eventListenerInfos) {
        int lastPriorityValue = eventListenerInfos.get(0).eventListenerAnnotation.pritority().value;

        for (EventListenerInfo eventListenerInfo : eventListenerInfos) {
            int actualPriorityValue = eventListenerInfo.eventListenerAnnotation.pritority().value;

            if(lastPriorityValue < actualPriorityValue){
                return false;
            }

            lastPriorityValue = actualPriorityValue;
        }

        return true;
    }


}
