package es.jaime;

import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.*;

public final class EventsListenersMapper {
    private final Map<Class<? extends Event>, Set<EventListenerInfo>> indexedEventListeners;
    private final Map<Class<?>, Object> instances;

    public EventsListenersMapper(String packageToScan) {
        this.indexedEventListeners = new HashMap<>();
        this.instances = new HashMap<>();

        this.searchForListeners(packageToScan);
    }

    private void searchForListeners (String packageToScan) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new MethodAnnotationsScanner())
                .setUrls(ClasspathHelper.forPackage(packageToScan)));

        Set<Method> methodsListeners = reflections.getMethodsAnnotatedWith(EventListener.class);

        for(Method method : methodsListeners) {
            checkParametersAndAdd(method.getParameterTypes(), method);
        }
    }

    private void checkParametersAndAdd(Class<?>[] params, Method method) {
        for (Class<?> param : params) {
            if(Event.class.isAssignableFrom(param)) { //Subtype of event}
                addEventListener(method, (Class<? extends Event>) param);
            }
        }
    }

    @SneakyThrows
    private void addEventListener(Method method, Class<? extends Event> param) {
        Set<EventListenerInfo> methodsFound = indexedEventListeners.get(param);

        Object instance = this.instances.get(method.getDeclaringClass()) == null ?
                method.getDeclaringClass().newInstance() :
                this.instances.get(method.getDeclaringClass());

        if(methodsFound == null || methodsFound.size() == 0){
            indexedEventListeners.put(param, Sets.newHashSet(EventListenerInfo.of(instance, method)));
        }else{
            methodsFound.add(EventListenerInfo.of(instance, method));
        }
    }

    public Set<EventListenerInfo> searchEventListeners (Class<? extends Event> event) {
        return this.indexedEventListeners.get(event);
    }
}
