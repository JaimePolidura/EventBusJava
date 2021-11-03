package es.jaime;

import java.util.Arrays;

public final class ReflectionsUtils {
    private ReflectionsUtils () {}

    public static boolean containsInterface (Class<?> classToCheck, Class<?> interfaceToCheck) {
        return Arrays.asList(classToCheck.getInterfaces())
                .contains(interfaceToCheck);
    }
}
