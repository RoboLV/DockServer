package app.bundles.core.console.models.parsers;

import app.bundles.core.console.interfaces.CommandParserInterface;
import dock.framework.components.di.DIFactory;
import dock.framework.components.events.interfaces.EventsManagerInterface;

import java.util.Arrays;

public class BasicCommandParser implements CommandParserInterface {
    /**
     * Event manager instance
     */
    EventsManagerInterface eventsManager;

    public BasicCommandParser(EventsManagerInterface eventsManager) {
        this.eventsManager = eventsManager;
    }

    @Override
    public void parse(String command) {
        String[] commandParts = command.split(" ");
        commandParts = Arrays.copyOfRange(commandParts, 1, commandParts.length);

        if (commandParts.length > 0) {
            command = commandParts[0];
        }

        eventsManager.dispatch("console.input.command.parsed", command, commandParts);
    }
}
