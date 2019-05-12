package app.bundles.core.console.models.workers;

import dock.framework.components.events.interfaces.EventsManagerInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputConsoleListener implements Runnable {
    /**
     * Signal, that thread is alive
     */
    protected volatile boolean isRunning;

    /**
     * Event manager instance
     */
    protected volatile EventsManagerInterface eventsManager;

    public InputConsoleListener(EventsManagerInterface eventManager) {
        this.isRunning = true;
        this.eventsManager = eventManager;
    }

    /**
     * Entry point for IO console listener
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String command;
        System.out.print("> ");

        while (isRunning) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            try {
                if (reader.ready()) {
                    command = reader.readLine();
                    System.out.print("> ");

                    eventsManager.dispatch("console.input.command.source", command);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * On next update close thread
     */
    public void stopListening() {
        isRunning = false;
    }
}
