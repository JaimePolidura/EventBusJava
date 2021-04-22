package es.jaime;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public final class EventConsumer {
    private final EventsListenersMapper mapper;

    @SneakyThrows
    public void consume (Event event) {
        Set<Class<?>> interfacesAccumulator = new HashSet<>();

        Class<? extends Event> classEventCheck = event.getClass();

        while (classEventCheck != null && !classEventCheck.equals(Object.class)) {
            Set<EventListenerInfo> info = mapper.searchEventListeners(classEventCheck);

            interfacesAccumulator.addAll(Arrays.asList(classEventCheck.getInterfaces()));

            if(info == null || info.isEmpty()){
                classEventCheck = (Class<? extends Event>) classEventCheck.getSuperclass();
                continue;
            }

            //Checking for event superclasses event listener
            for (EventListenerInfo eventListenerInfo : info) {
                Object instance = eventListenerInfo.instance;
                Method method = eventListenerInfo.method;
                Class<?>[] interfaces = eventListenerInfo.interfacesNeedToImplement;

                if(checkIfContainsInterface(interfaces, interfacesAccumulator)){
                    method.invoke(instance, event);
                }

            }

            classEventCheck = (Class<? extends Event>) classEventCheck.getSuperclass();
        }
    }

    private boolean checkIfContainsInterface (Class<?>[] interfacesToImplement, Set<Class<?>> interfaceAccumulator) {
        if(interfacesToImplement == null || interfacesToImplement.length == 0){
            return true; //No interfaces
        }

        for(Class<?> interfaceToCheck : interfacesToImplement){
            if(interfaceAccumulator.contains(interfaceToCheck)){
                return true;
            }
        }

        return false;
    }
}
