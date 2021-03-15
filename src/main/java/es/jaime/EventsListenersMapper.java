package es.jaime;

import com.google.common.collect.Sets;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EventsListenersMapper {
    private final Map<Class<? extends Event>, Set<Method>> indexedEventListeners;

    public EventsListenersMapper() {
        this.indexedEventListeners = new HashMap<>();

        this.searchForListeners();
    }

    private void searchForListeners () {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new ));

        Set<Method> methodsListeners = reflections.getMethodsAnnotatedWith(EventListener.class);

        for(Method method : methodsListeners) {
            checkPameters(method.getParameterTypes(), method);
        }
    }

    private void checkPameters (Class<?>[] params, Method method) {
        for (Class<?> param : params) {
            if(param.isAssignableFrom(Event.class)) { //Subtype of event}
                addEventListener(method);
            }
        }
    }

    private void addEventListener(Method method) {
        Set<Method> methodsFound = indexedEventListeners.get(method.getDeclaringClass());

        if(methodsFound == null){
            indexedEventListeners.put((Class<? extends Event>) method.getDeclaringClass(), Sets.newHashSet(method));
        }else{
            methodsFound.add(method);
        }
    }

    public Set<Method> searchEventListeners (Class<? extends Event> event) {
        return this.indexedEventListeners.get(event);
    }
}