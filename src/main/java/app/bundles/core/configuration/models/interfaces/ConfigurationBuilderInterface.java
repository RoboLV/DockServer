package app.bundles.core.configuration.models.interfaces;

import app.bundles.core.configuration.models.ConfigurationValue;

import java.util.HashMap;

public interface ConfigurationBuilderInterface {
    /**
     * Define configuration scope.
     * Uniques scopes stores in different configuration files.
     *
     * @param scope
     */
    void setScope(String scope);

    /**
     * Add property for configuration.
     *
     * @param propertyName
     * @param defaultPropertyValue
     */
    void addProperty(String propertyName, String defaultPropertyValue);

    /**
     * Get scope
     *
     * @return
     */
    String getScope();

    /**
     * Get all properties as map
     *
     * @return
     */
    HashMap<String, ConfigurationValue<String>> getProperties();
}
