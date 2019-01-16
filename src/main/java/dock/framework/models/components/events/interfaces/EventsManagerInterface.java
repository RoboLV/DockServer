package dock.framework.models.components.events.interfaces;

import dock.framework.models.interfaces.CoreComponentInterface;

import java.lang.reflect.Method;

public interface EventsManagerInterface extends CoreComponentInterface {

    /**
     * Register event scope
     *
     * @param scope
     */
    void RegisterScope(String scope);

    /**
     * Register scope with parent scope.
     * Scope depends on parent scope.
     *
     * @param scope
     * @param parentScope
     */
    void RegisterScope(String scope, String parentScope);

    /**
     * Event which need to be dispatched
     *
     * @param eventName
     * @param args
     */
    void Dispatch(String eventName, Object ... args);

    /**
     * Bind event to observer callback method
     *
     * @param scope
     * @param eventName
     * @param observer
     * @param callback
     */
    void Bind(String scope, String eventName, Class observer, Method callback);
}
