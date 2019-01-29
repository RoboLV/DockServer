package dock.framework.models;

import dock.framework.models.components.bundles.interfaces.BundlesManagerInterface;
import dock.framework.models.components.events.interfaces.EventsManagerInterface;
import dock.framework.utils.DI.DISingleton;

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
        System.out.println("Starting dock application");

        try {
            // initialize core
            initialize();

            // program main loop
            process();
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
        eventsManager.dispatch("framework.initialize.after");
    }

    /**
     * Application main loop
     */
    private void process() {
        // Before process start
        eventsManager.dispatch("framework.process.before");

        try {
            while (isProcessRunning) {
                // On process action
                eventsManager.dispatch("framework.process.action");
                stopProcess();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // On process end
        eventsManager.dispatch("framework.process.after", isProcessRunning);
    }

    /**
     * Application destruct event
     */
    private void destruct() {
        // On destruct
        eventsManager.dispatch("framework.destruct");

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
