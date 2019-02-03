package app.bundles.core.console.models;

import dock.framework.CoreComponent;

public class ConsoleManager extends CoreComponent {

    public ConsoleManager() {
    }

    /**
     * Component initialization
     */
    @Override
    public void initialize() {
        super.initialize();
        System.out.println("wow");
    }

    /**
     * Component process update event
     */
    @Override
    public void update() {
        super.update();
    }

    /**
     * Component destruct event
     */
    @Override
    public void destruct() {
        super.destruct();
    }
}
