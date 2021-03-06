package dock.framework.components.events;

import dock.framework.CoreComponent;
import dock.framework.components.events.anotations.Observer;
import dock.framework.components.events.interfaces.EventConfigurationInterface;
import dock.framework.components.events.interfaces.EventObserverInterface;
import dock.framework.components.events.interfaces.EventsManagerInterface;
import dock.framework.components.di.DIFactory;
import dock.framework.components.di.anotations.DISingleton;
import dock.framework.components.events.interfaces.TypedObserverInterface;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@DISingleton
public class Manager extends CoreComponent implements EventsManagerInterface {

    /**
     * Observer instances map
     */
    protected HashMap<Class, EventObserverInterface> observerInstances;

    /**
     * Observer callback map
     *
     * HashMap<String, TreeMap<Integer, ArrayList<ObserverCallback>>>
     */
    protected HashMap<String, TreeMap<Integer, ArrayList<ObserverCallback>>> observerCallbacks;

    /**
     * Scope dependency map
     * K - Scope V - Scope parent
     */
    protected HashMap<String, ArrayList<String>> scopeMap;

    /**
     * DI factory builder
     */
    protected DIFactory diFactory;

    /**
     * Constructor
     */
    public Manager() {
        observerInstances = new HashMap<>();
        observerCallbacks = new HashMap<>();
        scopeMap = new HashMap<>();
        diFactory = new DIFactory();
    }

    /**
     * Component initialization
     */
    @Override
    public void initialize() {
        super.initialize();
    }

    /**
     * Component destruct event
     */
    @Override
    public void destruct() {
        super.destruct();
    }

    /**
     * Register event scope
     *
     * @param scope
     */
    public void registerScope(String scope) {
        registerScope(scope, scope);
    }

    /**
     * Register scope with parent scope.
     * Scope depends on parent scope.
     *
     * @param scope
     * @param parentScope
     */
    @Override
    public void registerScope(String scope, String parentScope) {
        if (!scopeMap.containsKey(scope)) {
            scopeMap.put(scope, new ArrayList<>());
        }

        if (scope != parentScope) {
            scopeMap.get(scope).add(parentScope);
        }

        // register string based observers
        Reflections reflections = new Reflections(scope, new SubTypesScanner(false), Thread.currentThread().getContextClassLoader());
        Set<Class<? extends EventObserverInterface>> types = reflections.getSubTypesOf(EventObserverInterface.class);

        for (Class<?> type : types) {
            for(Method method: type.getMethods()) {
                if (method.isAnnotationPresent(Observer.class)) {
                    String eventName = method.getAnnotation(Observer.class).value();
                    bind(scope, eventName, type, method);
                }
            }
        }
    }

    /**
     * Event which need to be dispatched
     *
     * @param eventName
     * @param args
     */
    @Override
    public void dispatch(String eventName, Object... args) {
        if (observerCallbacks.containsKey(eventName)) {
            TreeMap<Integer, ArrayList<ObserverCallback>> map = observerCallbacks.get(eventName);

            for (Map.Entry<Integer, ArrayList<ObserverCallback>> entry : map.entrySet()) {
                ArrayList<ObserverCallback> callbacks = entry.getValue();

                for (ObserverCallback callback : callbacks) {
                    Class aClass = callback.getaClass();
                    EventObserverInterface observerInstance = observerInstances.get(aClass);

                    try {
                        callback.getCallback().invoke(observerInstance, args);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Bind event to observer callback method
     *
     * @param scope
     * @param eventName
     * @param observer
     * @param callback
     */
    @Override
    public void bind(String scope, String eventName, Class<?> observer, Method callback) {
        int sortKey = _scopeOrder(scope);
        ObserverCallback observerCallback = new ObserverCallback(observer, callback, scope);

        if (!observerInstances.containsKey(observer)) {
            observerInstances.put(observer, (EventObserverInterface)diFactory.create(observer));
        }

        if (!observerCallbacks.containsKey(eventName)) {
            observerCallbacks.put(eventName, new TreeMap<>());
        }

        // Add normal compare
        if (!observerCallbacks.get(eventName).containsKey(sortKey)) {
            observerCallbacks.get(eventName).put(sortKey, new ArrayList<ObserverCallback>());
        }

        if (!observerCallbacks.get(eventName).get(sortKey).contains(observerCallback)) {
            observerCallbacks.get(eventName).get(sortKey).add(observerCallback);
        }
    }

    /**
     * Get given scope call order
     *
     * @param scope
     *
     * @return integer
     */
    protected int _scopeOrder(String scope) {
        int order = 0;

        while (true) {
            ArrayList<String>listScope = scopeMap.get(scope);

            if (listScope.size() == 0) {
                break;
            }

            order++;

            int deepest = 0;
            for (String parentScope: listScope) {
                int deep = _scopeOrder(parentScope);

                if (deep > deepest) {
                    deepest = deep;
                }
            }

            order+= deepest;
        }

        return order;
    }
}
