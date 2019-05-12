package dock.framework.components.di;

public class AbstractFactory {
    /**
     * Di factory instance
     */
    protected DIFactory diFactory;

    public AbstractFactory(DIFactory diFactory) {
        this.diFactory = diFactory;
    }

    /**
     * Create object and cast to generic type
     *
     * @param type
     * @return
     */
    public Object create(Class<?> type) {
        return type.cast(diFactory.create(type, new Object[] {}));
    }

    /**
     * Create object with custom params
     *
     * @param type
     * @param args
     * @return
     */
    public Object create(Class<?> type, Object... args) {
        // TODO: try find custom factory for typ and only
        return type.cast(diFactory.create(type, args));
    }
}
