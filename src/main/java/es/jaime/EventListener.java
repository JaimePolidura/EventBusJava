package es.jaime;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface EventListener {
    /**
     * If you want a events that needs to implement an interface
     * you can declare it in this array.
     */
    Class<?>[] value() default {};
}
