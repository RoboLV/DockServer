package dock.framework.models;

import dock.framework.models.components.bundles.interfaces.BundlesManagerInterface;
import dock.framework.models.components.events.interfaces.EventsManagerInterface;
import dock.framework.utils.DI.DIInject;
import dock.framework.utils.DI.DISingleton;

@DIInject
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
        eventsManager.RegisterScope("dock.framework");

        // Initialize bundle bundles manager
        bundlesManager.initialize();

        // Call other component initialization
        eventsManager.Dispatch("framework.initialize.after");
    }

    /**
     * Application main loop
     */
    private void process() {
        // Before process start
        eventsManager.Dispatch("framework.process.before");

        try {
            while (isProcessRunning) {
                // On process action
                eventsManager.Dispatch("framework.process.action");
                stopProcess();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // On process end
        eventsManager.Dispatch("framework.process.after", isProcessRunning);
    }

    /**
     * Application destruct event
     */
    private void destruct() {
        // On destruct
        eventsManager.Dispatch("framework.destruct");

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
