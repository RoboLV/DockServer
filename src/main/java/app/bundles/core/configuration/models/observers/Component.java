package app.bundles.core.configuration.models.observers;

import app.bundles.core.configuration.models.ConfigurationManager;
import dock.framework.components.events.anotations.Observer;
import dock.framework.components.events.interfaces.EventObserverInterface;

public class Component implements EventObserverInterface {
    /**
     * Configuration manager instance
     */
    protected ConfigurationManager configurationManager;

    public Component(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    /**
     * Initialize
     */
    @Observer("dock.initialize")
    public void componentInitialize() {
        configurationManager.initialize();
    }
}
