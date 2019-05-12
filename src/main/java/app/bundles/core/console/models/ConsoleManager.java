package app.bundles.core.console.models;

import app.bundles.core.console.interfaces.CommandParserInterface;
import app.bundles.core.console.models.parsers.BasicCommandParser;
import app.bundles.core.console.models.workers.InputConsoleListener;
import dock.framework.CoreComponent;
import dock.framework.components.di.DIFactory;

import java.util.ArrayList;
import java.util.List;

public class ConsoleManager extends CoreComponent {
    /**
     * Console listener instance
     */
    protected InputConsoleListener inputConsoleListener;

    /**
     * Thread of console reader
     */
    protected Thread inputConsoleListenerThread;

    /**
     * List of active command parsers
     */
    protected ArrayList<CommandParserInterface> commandParsers;

    /**
     * DiFactory
     */
    protected DIFactory diFactory;

    public ConsoleManager(InputConsoleListener inputConsoleListener, DIFactory diFactory) {
        this.inputConsoleListener = inputConsoleListener;
        this.diFactory = diFactory;
        commandParsers = new ArrayList<>();
    }

    /**
     * Component initialization
     */
    @Override
    public void initialize() {
        super.initialize();
        System.out.println("Console manager initialized");
        registerParser(BasicCommandParser.class);

        inputConsoleListenerThread = new Thread(inputConsoleListener);
        inputConsoleListenerThread.start();
    }

    /**
     * Component destruct event
     */
    @Override
    public void destruct() {
        inputConsoleListener.stopListening();
        super.destruct();
    }

    /**
     * Process imputed commands
     *
     * @param String command
     */
    public void commandInputObserver(String command) {
        for (CommandParserInterface parser : commandParsers) {
            parser.parse(command);
        }
    }

    /**
     * Register console command parser
     *
     * @param parser
     */
    public void registerParser(Class parser) {
        if (CommandParserInterface.class.isAssignableFrom(parser)) {
            registerParser((CommandParserInterface) diFactory.create(parser));
        }
    }

    /**
     * Register console command parser
     *
     * @param parser
     */
    public void registerParser(CommandParserInterface parser) {
        commandParsers.add(parser);
    }
}
