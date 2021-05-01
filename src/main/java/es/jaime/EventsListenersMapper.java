package es.jaime;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

public final class EventsListenersMapper {
    private final Map<Class<? extends Event>, List<EventListenerInfo>> indexedEventListeners;
    private final Map<Class<?>, Object> alreadyInstanciedEventListeners;

    public EventsListenersMapper(String packageToScan) {
        this.indexedEventListeners = new HashMap<>();
        this.alreadyInstanciedEventListeners = new HashMap<>();

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

        sortEventListenerInfoByPriority();
    }

    private void checkParametersAndAdd(Class<?>[] params, Method method) {
        for (Class<?> param : params) {
            if(Event.class.isAssignableFrom(param)) { //Subtype of event
                addEventListener(method, (Class<? extends Event>) param);
            }
        }
    }

    private void sortEventListenerInfoByPriority () {
        for(Map.Entry<Class<? extends Event>, List<EventListenerInfo>> entry : indexedEventListeners.entrySet()){
            entry.getValue().sort(EventListenerInfo::compareTo);
        }
    }

    @SneakyThrows
    private void addEventListener(Method method, Class<? extends Event> event) {
        Object instance = this.alreadyInstanciedEventListeners.get(method.getDeclaringClass()) == null ?
                method.getDeclaringClass().newInstance() :
                this.alreadyInstanciedEventListeners.get(method.getDeclaringClass());

        EventListener eventListenerInfo = this.getEventListenerAnnotationFromMethod(method);

        List<EventListenerInfo> eventListeners = indexedEventListeners.get(event);

        if(eventListeners == null || eventListeners.size() == 0){
            List<EventListenerInfo> list = new LinkedList<>();
            list.add(EventListenerInfo.of(instance, method, eventListenerInfo));

            indexedEventListeners.put(event, list);
        }else{
            eventListeners.add(EventListenerInfo.of(instance, method, eventListenerInfo));
        }
    }

    private EventListener getEventListenerAnnotationFromMethod (Method method) {
        return (EventListener) Stream.of(method.getAnnotations())
                .filter(annotation -> annotation instanceof EventListener)
                .findAny()
                .get();
    }

    public List<EventListenerInfo> searchEventListeners (Class<? extends Event> event) {
        return this.indexedEventListeners.get(event);
    }
}
