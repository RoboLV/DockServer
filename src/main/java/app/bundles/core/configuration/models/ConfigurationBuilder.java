package app.bundles.core.configuration.models;

import app.bundles.core.configuration.models.factory.ConfigurationValueFactory;
import app.bundles.core.configuration.models.interfaces.ConfigurationBuilderInterface;
import dock.framework.components.di.AbstractFactory;

import java.util.HashMap;

public abstract class ConfigurationBuilder implements ConfigurationBuilderInterface {
    /**
     * Scope
     */
    protected String scope;

    /**
     * Values
     */
    protected HashMap<String, ConfigurationValue<String>> values;

    /**
     * Configuration value factory
     */
    protected ConfigurationValueFactory<String> configurationValueFactory;

    /**
     * Constructor
     *
     * @param configurationValueFactory
     */
    public ConfigurationBuilder(ConfigurationValueFactory<String> configurationValueFactory) {
        this.configurationValueFactory = configurationValueFactory;
    }

    /**
     * Prepare configuration structure
     */
    public void prepare() {
    }

    /**
     * Define configuration scope.
     * Uniques scopes stores in different configuration files.
     *
     * @param scope
     */
    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Add property for configuration.
     *
     * @param propertyName
     * @param defaultPropertyValue
     */
    @Override
    public void addProperty(String propertyName, String defaultPropertyValue) {
        ConfigurationValue<String> value = configurationValueFactory.create();

        value.setValue(defaultPropertyValue);

        values.put(propertyName, value);
    }

    /**
     * Get scope
     *
     * @return
     */
    @Override
    public String getScope() {
        return scope;
    }

    /**
     * Get all properties as map
     *
     * @return
     */
    @Override
    public HashMap<String, ConfigurationValue<String>> getProperties() {
        return values;
    }
}
