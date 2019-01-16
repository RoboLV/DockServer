package dock.framework.models.components.bundles;

import dock.Main;
import dock.framework.models.CoreComponent;
import dock.framework.models.components.bundles.interfaces.BundlesManagerInterface;
import dock.framework.models.components.events.interfaces.EventsManagerInterface;
import dock.framework.utils.DI.DISingleton;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

@DISingleton
public class Manager extends CoreComponent implements BundlesManagerInterface {

    protected Document configuration;

    protected EventsManagerInterface eventsManager;

    public Manager(EventsManagerInterface eventsManager) {
        this.eventsManager = eventsManager;
    }

    /**
     * Component initialization
     */
    @Override
    public void initialize() {
        super.initialize();

        _loadBundlesConfiguration();
        _initializeBundles();
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

    private void _loadBundlesConfiguration() {
        InputStream cfg = getClass().getResourceAsStream("/resources/bundles.xml");
        InputStream cfgIs = Main.class.getClassLoader().getResourceAsStream("dock/app/bundles/bundles.xml");

        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            configuration = docBuilder.parse(cfgIs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void _initializeBundles() {

    }
}
