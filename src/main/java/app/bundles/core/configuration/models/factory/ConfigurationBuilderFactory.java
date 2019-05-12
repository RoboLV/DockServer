package app.bundles.core.configuration.models.factory;

import app.bundles.core.configuration.models.ConfigurationBuilder;
import dock.framework.components.di.AbstractFactory;
import dock.framework.components.di.DIFactory;

public class ConfigurationBuilderFactory extends AbstractFactory {

    public ConfigurationBuilderFactory(DIFactory diFactory) {
        super(diFactory);
    }

    /**
     * Create
     *
     * @return
     */
    public ConfigurationBuilder create() {
        return (ConfigurationBuilder) create(ConfigurationBuilder.class);
    }
}
