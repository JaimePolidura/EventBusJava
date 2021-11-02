package es.jaime;

import java.util.Arrays;

public final class ReflectionsUtils {
    private ReflectionsUtils () {}

    public static boolean containsInterface (Class<?> classToCheck, Class<?> interfaceToCheck) {
        Class<?>[] interfacesOfClass = classToCheck.getInterfaces();

        return Arrays.asList(interfacesOfClass).contains(interfaceToCheck);
    }
}
