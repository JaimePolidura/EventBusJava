package es.jaime.test;

import es.jaime.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MyCustomEvent extends Event {
    @Getter private final String name;
}
