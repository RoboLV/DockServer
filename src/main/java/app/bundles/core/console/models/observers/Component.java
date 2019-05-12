package app.bundles.core.console.models.observers;

import app.bundles.core.console.models.ConsoleManager;
import dock.framework.components.events.anotations.Observer;
import dock.framework.components.events.interfaces.EventObserverInterface;

public class Component implements EventObserverInterface {

    protected ConsoleManager consoleManager;

    public Component(ConsoleManager consoleManager) {
        this.consoleManager = consoleManager;
    }

    @Observer("dock.initialize")
    public void initialize() {
        consoleManager.initialize();
    }

    @Observer("console.input.command.source")
    public void onConsoleCommandReceived(String command) {
        consoleManager.commandInputObserver(command);
    }

    @Observer("console.input.command.parsed")
    public void commandPrinter(String command, String[] parts, String[] m) {
        System.out.println(command);
    }
}
