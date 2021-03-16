package es.jaime;

import com.google.common.collect.Sets;
import javassist.URLClassPath;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
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
                .setScanners(new MethodAnnotationsScanner())
                .setUrls(ClasspathHelper.forPackage("")));

        Set<Method> methodsListeners = reflections.getMethodsAnnotatedWith(EventListener.class);

        for(Method method : methodsListeners) {
            checkPameters(method.getParameterTypes(), method);
        }
    }

    private void checkPameters (Class<?>[] params, Method method) {
        for (Class<?> param : params) {
            if(Event.class.isAssignableFrom(param)) { //Subtype of event}
                addEventListener(method, (Class<? extends Event>) param);
            }
        }
    }

    private void addEventListener(Method method, Class<? extends Event> param) {
        Set<Method> methodsFound = indexedEventListeners.get(param);

        if(methodsFound == null){
            indexedEventListeners.put(param, Sets.newHashSet(method));
        }else{
            methodsFound.add(method);
        }
    }

    public Set<Method> searchEventListeners (Class<? extends Event> event) {
        return this.indexedEventListeners.get(event);
    }
}
