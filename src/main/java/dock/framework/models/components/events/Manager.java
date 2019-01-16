package dock.framework.models.components.events;

import dock.framework.models.CoreComponent;
import dock.framework.models.components.events.interfaces.EventsManagerInterface;
import dock.framework.utils.DI.DISingleton;

import java.lang.reflect.Method;

@DISingleton
public class Manager extends CoreComponent implements EventsManagerInterface {

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
    @Override
    public void RegisterScope(String scope) {
        RegisterScope(scope, scope);
    }

    /**
     * Register scope with parent scope.
     * Scope depends on parent scope.
     *
     * @param scope
     * @param parentScope
     */
    @Override
    public void RegisterScope(String scope, String parentScope) {
    }

    /**
     * Event which need to be dispatched
     *
     * @param eventName
     * @param args
     */
    @Override
    public void Dispatch(String eventName, Object... args) {

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
    public void Bind(String scope, String eventName, Class observer, Method callback) {

    }
}
