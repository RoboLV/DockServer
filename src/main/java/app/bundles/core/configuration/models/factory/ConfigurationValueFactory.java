package app.bundles.core.configuration.models.factory;

import app.bundles.core.configuration.models.ConfigurationValue;
import dock.framework.components.di.AbstractFactory;
import dock.framework.components.di.DIFactory;

public class ConfigurationValueFactory<Type> extends AbstractFactory {

    public ConfigurationValueFactory(DIFactory diFactory) {
        super(diFactory);
    }

    public ConfigurationValue<Type> create() {
        return (ConfigurationValue<Type>) create(ConfigurationValue.class);
    }
}
