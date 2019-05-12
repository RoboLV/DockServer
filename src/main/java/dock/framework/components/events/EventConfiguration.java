package dock.framework.components.events;

import dock.framework.components.events.interfaces.EventConfigurationInterface;

public class EventConfiguration implements EventConfigurationInterface {
    /**
     * Can run observers in async mode?
     */
    protected boolean isAsync = false;

    public boolean isAsync() {
        return isAsync;
    }

    public EventConfiguration setAsync(boolean async) {
        isAsync = async;
        return this;
    }
}
