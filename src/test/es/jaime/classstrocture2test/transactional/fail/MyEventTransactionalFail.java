package es.jaime.classstrocture2test.transactional.fail;

import es.jaime.Event;

public final class MyEventTransactionalFail extends Event {
    @Override
    public boolean isTransactional() {
        return true;
    }
}
