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
    private EventListenerDependencyProvider eventListenerDependencyProvider;

    public SimpleEventListenersMapper(EventListenerDependencyProvider eventListenerDependencyProvider) {
        this.eventListenerDependencyProvider = eventListenerDependencyProvider;
    }

    public SimpleEventListenersMapper() {}

    public void scan(String packageToScan) {
        this.searchForListeners(packageToScan);
    }

    public List<EventListenerInfo> findEventListenerInfoByEvent(Class<? extends Event> event) {
        return this.indexedEventListenersInfo.get(event);
    }

    private void searchForListeners (String packageToScan) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new MethodAnnotationsScanner())
                .setUrls(ClasspathHelper.forPackage(packageToScan)));

        Set<Method> methodsListeners = reflections.getMethodsAnnotatedWith(EventListener.class);

        for(Method method : methodsListeners) {
            Class<? extends Event> getEventListened = this.getEventListened(method);
            addEventListener(method, getEventListened);
        }

        sortEventListenerInfoByPriority();
    }

    private Class<? extends Event> getEventListened(Method method) {
        for (Class<?> param : method.getParameterTypes()) {
            if(Event.class.isAssignableFrom(param)) { //Subtype of event
                return (Class<? extends Event>) param;
            }else{
                break;
            }
        }

        throw new IllegalArgumentException(String.format("Method %s on class %s should have a parameter that implements Event",
                method.getName(), method.getDeclaringClass().getName()));
    }

    private void sortEventListenerInfoByPriority () {
        for(Map.Entry<Class<? extends Event>, List<EventListenerInfo>> entry : indexedEventListenersInfo.entrySet()){
            entry.getValue().sort(EventListenerInfo::compareTo);
        }
    }

    private void addEventListener(Method method, Class<? extends Event> event) {
        Object instanceEventListener = this.getEventListenerInfo(method.getDeclaringClass());
        EventListener eventListenerInfo = this.getEventListenerAnnotationFromMethod(method);
        List<EventListenerInfo> eventListenersInfo = indexedEventListenersInfo.get(event);

        if(eventListenersInfo == null || eventListenersInfo.size() == 0){
            List<EventListenerInfo> list = new LinkedList<>();
            list.add(EventListenerInfo.of(instanceEventListener, method, eventListenerInfo));

            indexedEventListenersInfo.put(event, list);
        }else{
            eventListenersInfo.add(EventListenerInfo.of(instanceEventListener, method, eventListenerInfo));
        }
    }

    private Object getEventListenerInfo(Class<?> clazz) {
        if(eventListenerDependencyProvider != null){
            return getInstanceFromEventListenerInstanceProvider(clazz);
        }

        return this.alreadyInstancedEventListeners.containsKey(clazz) ?
                this.alreadyInstancedEventListeners.get(clazz) :
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
