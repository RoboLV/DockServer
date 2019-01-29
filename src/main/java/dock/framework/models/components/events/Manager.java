package dock.framework.models.components.events;

import dock.framework.models.CoreComponent;
import dock.framework.models.components.events.anotations.Observer;
import dock.framework.models.components.events.interfaces.EventObserverInterface;
import dock.framework.models.components.events.interfaces.EventsManagerInterface;
import dock.framework.utils.DI.DIFactory;
import dock.framework.utils.DI.DISingleton;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.annotation.Annotation;
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
     * HashMap<event name, TreeMap<sort key, ObserverCallback>>
     */
    protected HashMap<String, TreeMap<Integer, ObserverCallback>> observerCallbacks;

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
     * Component process update event
     */
    @Override
    public void update() {
        super.update();
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
            TreeMap<Integer, ObserverCallback> map = observerCallbacks.get(eventName);

            for (Map.Entry<Integer, ObserverCallback> entry : map.entrySet()) {
                ObserverCallback callback = entry.getValue();

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

        observerCallbacks.get(eventName).put(sortKey, observerCallback);
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
