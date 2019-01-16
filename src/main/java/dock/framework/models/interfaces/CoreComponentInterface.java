package dock.framework.models.interfaces;

public interface CoreComponentInterface {
    /**
     * Component initialization
     */
    void initialize();

    /**
     * Component process update event
     */
    void update();

    /**
     * Component destruct event
     */
    void destruct();
}
