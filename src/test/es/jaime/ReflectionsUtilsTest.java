package es.jaime;

import io.vavr.control.Try;
import org.junit.Test;

import static org.junit.Assert.*;

public final class ReflectionsUtilsTest {
    @Test
    public void containsInterfaceTest () {
        assertTrue(ReflectionsUtils.containsInterface(ClassToCheck.class, A.class));
        assertFalse(ReflectionsUtils.containsInterface(ClassToCheck.class, B.class));
        assertFalse(ReflectionsUtils.containsInterface(ClassToCheck.class, C.class));
    }


    private class ClassToCheck implements A{

    }

    interface A {}
    interface B {}
    interface C {}
}
