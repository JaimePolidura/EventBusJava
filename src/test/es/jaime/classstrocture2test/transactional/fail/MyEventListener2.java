package es.jaime.classstrocture2test.transactional.fail;

import es.jaime.EventListener;
import es.jaime.TransactionalEventListener;
import es.jaime.classstrocture2test.transactional.MyEventListenerTransactional;

public final class MyEventListener2 extends MyEventListenerTransactional implements TransactionalEventListener {
    @EventListener
    public void on(MyEventTransactionalFail event) {
        incrementCounter();

        //Failed
        Integer.parseInt("xd");
    }

    @Override
    public void rollback() {
        decrementCounter();
    }
}
