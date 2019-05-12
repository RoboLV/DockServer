package app.bundles.core.configuration.models;

import app.bundles.core.configuration.models.factory.ConfigurationBuilderFactory;
import app.bundles.core.web.server.models.Configuration;
import dock.framework.BaseComponent;
import dock.framework.components.di.DIFactory;
import dock.framework.components.di.anotations.DISingleton;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

@DISingleton
public class ConfigurationManager {
    /**
     * Path where all configurations stored
     */
    protected String configurationPath;

    /**
     * All configuration storage
     */
    protected HashMap<String, HashMap<String, ConfigurationValue<?>>> configurationHashMap;

    /**
     * Di factory instance
     */
    protected DIFactory diFactory;

    /**
     * Configuration build factory
     */
    protected ConfigurationBuilderFactory configurationBuilderFactory;

    /**
     * Constructor
     */
    public ConfigurationManager(DIFactory diFactory, ConfigurationBuilderFactory configurationBuilderFactory) {
        this.diFactory = diFactory;
        this.configurationBuilderFactory = configurationBuilderFactory;

        configurationHashMap = new HashMap<>();
    }

    /**
     * Initialize component
     */
    public void initialize() {
        _resolveConfigurationPath();
        _prepareConfiguration();
    }

    /**
     * Prepare configuration structure
     */
    protected void _prepareConfiguration() {
        Reflections reflections = new Reflections("", new SubTypesScanner(false), Thread.currentThread().getContextClassLoader());
        Set<Class<? extends ConfigurationBuilder>> configurations = reflections.getSubTypesOf(ConfigurationBuilder.class);
        HashMap<String, ConfigurationValue<String>> propertyMap;
        String scope;

        for (Class<? extends ConfigurationBuilder> configuration : configurations) {
            ConfigurationBuilder builder = configurationBuilderFactory.create();
            builder.prepare();
            scope = builder.getScope();
            propertyMap = builder.getProperties();

            if (!configurationHashMap.containsKey(scope)) {
                configurationHashMap.put(scope, new HashMap<>());
            }

            configurationHashMap.get(scope).putAll(propertyMap);
        }
    }

    /**
     * Find exact configuration directory
     */
    protected void _resolveConfigurationPath() {
        File localPath = new File("config/");

        if (!localPath.exists()) {
            try {
                localPath.mkdirs();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        configurationPath = localPath.getAbsolutePath();
    }
}
