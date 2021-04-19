package es.jaime.test;

import lombok.Getter;

public final class MyCustomEvent2 extends MyCustomEvent{
    @Getter private final String apellido;

    public MyCustomEvent2(String name, String apellido) {
        super(name);
        this.apellido = apellido;
    }
}
