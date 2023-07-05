package es.jaime;

import io.vavr.control.Try;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

public final class SimpleEventListenersMapper {
    private final Map<Class<? extends Event>, List<EventListenerInfo>> indexedEventListenersInfo = new HashMap<>();
    private final Map<Class<?>, Object> alreadyInstancedEventListeners = new HashMap<>();
    private final String commonPackage;

    private EventListenerDependencyProvider eventListenerDependencyProvider;


    public SimpleEventListenersMapper(EventListenerDependencyProvider eventListenerDependencyProvider, String commonPackage) {
        this.eventListenerDependencyProvider = eventListenerDependencyProvider;
        this.commonPackage = commonPackage;
    }

    public SimpleEventListenersMapper(String commonPackage) {
        this.commonPackage = commonPackage;
    }

    public List<EventListenerInfo> findEventListenerInfoByEvent(Class<? extends Event> event) {
        return indexedEventListenersInfo.get(event);
    }

    public void scanForListeners() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new MethodAnnotationsScanner())
                .setUrls(ClasspathHelper.forPackage(commonPackage)));

        Set<Method> methodsListeners = reflections.getMethodsAnnotatedWith(EventListener.class);

        for(Method method : methodsListeners) {
            Class<? extends Event> getEventListened = getEventListened(method);
            addEventListener(method, getEventListened);
        }

        sortEventListenerInfoByPriority();
    }

    private Class<? extends Event> getEventListened(Method method) {
        if(method.getParameterTypes().length == 0){
            throw new IllegalArgumentException(String.format("Method %s on class %s doest listen to any event",
                    method.getName(), method.getDeclaringClass().getName()));
        }

        if(!Event.class.isAssignableFrom(method.getParameterTypes()[0])){
            throw new IllegalArgumentException(String.format("Method %s on class %s should have a parameter that implements Event",
                    method.getName(), method.getDeclaringClass().getName()));
        }

        return (Class<? extends Event>) method.getParameterTypes()[0];
    }

    private void sortEventListenerInfoByPriority () {
        for(Map.Entry<Class<? extends Event>, List<EventListenerInfo>> entry : indexedEventListenersInfo.entrySet()){
            entry.getValue().sort(EventListenerInfo::compareTo);
        }
    }

    private void addEventListener(Method method, Class<? extends Event> event) {
        Object instanceEventListener = getInstanceOfEventListener(method.getDeclaringClass());
        EventListener eventListenerInfo = getEventListenerAnnotationFromMethod(method);
        List<EventListenerInfo> eventListenersInfo = indexedEventListenersInfo.get(event);

        if(eventListenersInfo == null || eventListenersInfo.size() == 0){
            List<EventListenerInfo> list = new LinkedList<>();
            list.add(EventListenerInfo.of(instanceEventListener, method, eventListenerInfo));

            indexedEventListenersInfo.put(event, list);
        }else{
            eventListenersInfo.add(EventListenerInfo.of(instanceEventListener, method, eventListenerInfo));
        }
    }

    private Object getInstanceOfEventListener(Class<?> clazz) {
        if(eventListenerDependencyProvider != null){
            return getInstanceFromEventListenerInstanceProvider(clazz);
        }

        return alreadyInstancedEventListeners.containsKey(clazz) ?
                alreadyInstancedEventListeners.get(clazz) :
                Try.of(clazz::newInstance).get();
    }

    private Object getInstanceFromEventListenerInstanceProvider(Class<?> clazz) {
        Object fromProvider = this.eventListenerDependencyProvider.get(clazz);
        if (fromProvider == null) {
            throw new RuntimeException(String.format("Unknown dependency %s", clazz.getName()));
        }

        return fromProvider;
    }

    private EventListener getEventListenerAnnotationFromMethod (Method method) {
        return (EventListener) Stream.of(method.getAnnotations())
                .filter(annotation -> annotation instanceof EventListener)
                .findAny()
                .get();
    }
}
