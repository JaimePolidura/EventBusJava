package es.jaime.classstrocture2test.transactional.success;

import es.jaime.Event;

public final class MyEventTransactionalSuccess extends Event {
    @Override
    public boolean isTransactional() {
        return true;
    }
}
