package app.bundles.core.web.server.models.configuration;

import app.bundles.core.configuration.models.ConfigurationBuilder;
import app.bundles.core.configuration.models.ConfigurationValue;
import app.bundles.core.configuration.models.factory.ConfigurationValueFactory;
import dock.framework.components.di.AbstractFactory;

public class Structure extends ConfigurationBuilder {
    /**
     * Constructor
     *
     * @param configurationValueFactory
     */
    public Structure(ConfigurationValueFactory<String> configurationValueFactory) {
        super(configurationValueFactory);
    }

    /**
     * Prepare configuration structure
     */
    @Override
    public void prepare() {
        setScope("web.server");

        addProperty("threadCount", "1");
        addProperty("restartThreadAfter", "1000");
        addProperty("maxExecutionTime", "60000");
        addProperty("port", "80");
    }
}
