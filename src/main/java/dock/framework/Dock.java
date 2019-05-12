package dock.framework;

import dock.framework.components.bundles.interfaces.BundlesManagerInterface;
import dock.framework.components.events.interfaces.EventsManagerInterface;
import dock.framework.components.di.anotations.DISingleton;

@DISingleton
public class Dock {

    private boolean isProcessRunning = true;

    private BundlesManagerInterface bundlesManager;

    private EventsManagerInterface eventsManager;

    public Dock(BundlesManagerInterface bundlesManager, EventsManagerInterface eventsManager) {
        this.bundlesManager = bundlesManager;
        this.eventsManager = eventsManager;
    }

    /**
     * Run application
     */
    public void run() {
        try {
            // initialize core
            initialize();

            // program main loop
            process();
        } catch (Exception e) {
            eventsManager.dispatch("dock.error.fatal", e);
            System.out.println(e.getMessage());
        } finally {
            // proper program close
            destruct();
        }
    }

    /**
     * Initialize application bundles
     */
    private void initialize() {
        // Initialize Events manager component
        eventsManager.initialize();
        eventsManager.registerScope("dock");

        // Initialize bundle bundles manager
        bundlesManager.initialize();

        // Call other component initialization
        eventsManager.dispatch("dock.initialize");
    }

    /**
     * Application main loop
     */
    private void process() {
        // Before process start
        eventsManager.dispatch("dock.process.before");

        try {
            while (isProcessRunning) {
                eventsManager.dispatch("dock.process");

                Thread.sleep(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Application destruct event
     */
    private void destruct() {
        // On destruct
        eventsManager.dispatch("dock.destruct");

        eventsManager.destruct();
        bundlesManager.destruct();
    }

    /**
     * Stop main process
     */
    public void stopProcess() {
        isProcessRunning = false;
    }
}
