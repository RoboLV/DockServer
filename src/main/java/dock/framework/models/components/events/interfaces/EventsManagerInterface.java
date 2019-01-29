package dock.framework.models.components.events.interfaces;

import dock.framework.models.interfaces.CoreComponentInterface;

import java.lang.reflect.Method;

public interface EventsManagerInterface extends CoreComponentInterface {

    /**
     * Register event scope
     *
     * @param scope
     */
    void registerScope(String scope);

    /**
     * Register scope with parent scope.
     * Scope depends on parent scope.
     *
     * @param scope
     * @param parentScope
     */
    void registerScope(String scope, String parentScope);

    /**
     * Event which need to be dispatched
     *
     * @param eventName
     * @param args
     */
    void dispatch(String eventName, Object ... args);

    /**
     * bind event to observer callback method
     *
     * @param scope
     * @param eventName
     * @param observer
     * @param callback
     */
    void bind(String scope, String eventName, Class<?> observer, Method callback);
}
