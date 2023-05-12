package es.jaime.classstrocture2test.transactional.success;

import es.jaime.EventListener;
import es.jaime.classstrocture2test.transactional.MyEventListenerTransactional;
import es.jaime.classstrocture2test.transactional.fail.MyEventTransactionalFail;

public final class MyEventListener1 extends MyEventListenerTransactional implements TransactionalEventListener {
    @EventListener
    public void on(MyEventTransactionalSuccess event) {
        incrementCounter();
    }

    @Override
    public void rollback() {
        decrementCounter();
    }
}
