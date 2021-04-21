package es.jaime;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

public interface EventBus {
    void publish (@NonNull Collection<? extends Event> events);
    void publish (@NonNull Event event);

    @SneakyThrows
    default void consumeEvent (Event event, EventsListenersMapper mapper) {
        Class<? extends Event> classEventCheck = event.getClass();

        while (classEventCheck != null) {
            Set<EventListenerInfo> info = mapper.searchEventListeners(classEventCheck);

            if(info == null || info.isEmpty()){
                classEventCheck = (Class<? extends Event>) classEventCheck.getSuperclass();
                continue;
            }

            //Checking for event superclasses event listener
            for (EventListenerInfo eventListenerInfo : info) {
                Object instance = eventListenerInfo.instance;
                Method method = eventListenerInfo.method;
                Class<?>[] interfaces = eventListenerInfo.interfaces;

                if(checkIfContainsInterface(event.getClass(), interfaces)){
                    method.invoke(instance, event);
                }

            }

            classEventCheck = (Class<? extends Event>) classEventCheck.getSuperclass();
        }
    }

    default boolean checkIfContainsInterface (Class<? extends Event> eventClass, Class<?>[] interfaces) {
        if(interfaces == null || interfaces.length == 0 || interfaces[0] == Event.class){
            return true; //No interfaces
        }

        Class<?>[] eventClassInterfaces = eventClass.getInterfaces();

        for(int i = 0; i < eventClassInterfaces.length; i++){
            Class<?> interfaceEventToCheck = eventClassInterfaces[i];

            for(int j = 0; j < interfaces.length; j++){
                if(interfaceEventToCheck == interfaces[j]){
                    return true;
                }
            }
        }

        return false;
    }
}
