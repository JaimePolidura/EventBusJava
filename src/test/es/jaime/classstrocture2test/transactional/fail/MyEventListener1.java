package es.jaime.classstrocture2test.transactional.fail;

import es.jaime.EventListener;
import es.jaime.classstrocture2test.transactional.MyEventListenerTransactional;

public final class MyEventListener1 extends MyEventListenerTransactional implements TransactionalEventListener {
    @EventListener
    public void on(MyEventTransactionalFail event) {
        incrementCounter();
    }

    @Override
    public void rollback() {
        decrementCounter();
    }
}
