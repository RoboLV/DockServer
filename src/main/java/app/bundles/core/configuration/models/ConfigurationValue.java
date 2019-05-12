package app.bundles.core.configuration.models;

public class ConfigurationValue<ValueType> {
    /**
     * Value storage
     */
    private ValueType value;

    public ConfigurationValue(ValueType value) {
        this.value = value;
    }

    /**
     * Get value from configuration
     *
     * @return
     */
    public ValueType getValue() {
        return value;
    }

    /**
     * Set configuration value
     *
     * @param value
     * @return
     */
    public ConfigurationValue<ValueType> setValue(ValueType value) {
        this.value = value;
        return this;
    }
}
