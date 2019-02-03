package dock;

import dock.framework.Dock;
import dock.framework.components.di.DIFactory;

public class Main {
    public static void main(final String[] args) throws Exception {
        // Use dependency injection factory
        DIFactory diFactory = new DIFactory();

        Dock appDock = (Dock)diFactory.create(Dock.class);
        appDock.run();
    }
}