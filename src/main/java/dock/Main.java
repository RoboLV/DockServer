package dock;

import dock.framework.models.Dock;
import dock.framework.utils.DI.DIFactory;

public class Main {
    public static void main(final String[] args) throws Exception {
        // Use dependency injection factory
        DIFactory diFactory = new DIFactory();

        Dock appDock = (Dock)diFactory.create(Dock.class);
        appDock.run();
    }
}