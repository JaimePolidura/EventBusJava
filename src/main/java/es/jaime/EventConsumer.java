package es.jaime;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Set;

@AllArgsConstructor
public final class EventConsumer {
    private final EventsListenersMapper mapper;

    @SneakyThrows
    public void consume (Event event) {
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
                Class<?>[] interfaces = eventListenerInfo.interfacesNeedToImplement;

                if(checkIfContainsInterface(event.getClass(), interfaces)){
                    method.invoke(instance, event);
                }

            }

            classEventCheck = (Class<? extends Event>) classEventCheck.getSuperclass();
        }
    }

    private boolean checkIfContainsInterface (Class<? extends Event> eventClass, Class<?>[] interfaces) {
        if(interfaces == null || interfaces.length == 0){
            return true; //No interfaces
        }

        Class<?>[] eventClassInterfaces = eventClass.getInterfaces();

        for (Class<?> interfaceEventToCheck : eventClassInterfaces) {
            for (Class<?> anInterface : interfaces) {
                if (interfaceEventToCheck == anInterface) {
                    return true;
                }
            }
        }

        return false;
    }
}
