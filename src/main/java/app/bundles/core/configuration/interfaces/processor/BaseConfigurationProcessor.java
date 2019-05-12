package app.bundles.core.configuration.interfaces.processor;

public interface BaseConfigurationProcessor {

    /**
     * Parse given file
     *
     * @param file
     * @return
     */
    boolean parse(String file);

    /**
     * Prepare to save given scope
     *
     * @param scope
     * @return
     */
    String prepare(String scope);
}
