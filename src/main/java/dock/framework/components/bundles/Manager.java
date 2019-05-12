package dock.framework.components.bundles;

import dock.Main;
import dock.framework.BaseBundle;
import dock.framework.BaseComponent;
import dock.framework.CoreComponent;
import dock.framework.Dock;
import dock.framework.components.bundles.interfaces.BundlesManagerInterface;
import dock.framework.components.di.DIFactory;
import dock.framework.components.events.interfaces.EventObserverInterface;
import dock.framework.components.events.interfaces.EventsManagerInterface;
import dock.framework.components.di.anotations.DISingleton;
import dock.framework.interfaces.BaseBundleInterface;
import dock.framework.interfaces.BaseComponentInterface;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@DISingleton
public class Manager extends CoreComponent implements BundlesManagerInterface {

    protected Document configuration;

    protected EventsManagerInterface eventsManager;

    protected Dock dockInstance;

    protected DIFactory diInstance;

    /**
     * Store all initialize Bundles
     */
    protected List<BaseBundleInterface> bundles = new ArrayList<>();

    /**
     * Store all initialized components
     */
    protected List<BaseComponentInterface> components = new ArrayList<>();

    public Manager(EventsManagerInterface eventsManager, Dock dock, DIFactory diFactory) {
        this.eventsManager = eventsManager;
        this.dockInstance = dock;
        this.diInstance = diFactory;
    }

    /**
     * Component initialization
     */
    @Override
    public void initialize() {
        super.initialize();

        if (dockInstance == null) {
            dockInstance = (Dock) diInstance.create(Dock.class);
        }

        // TODO: Load all external bundles/components there
        _loadBundlesConfiguration();
        _initializeBundles();
    }

    /**
     * Component destruct event
     */
    @Override
    public void destruct() {
        super.destruct();
    }

    /**
     * Load configuration && build merge configuration
     */
    private void _loadBundlesConfiguration() {
        List<BaseBundleInterface> unsortedBundleList = new ArrayList<>();
        boolean needProcess = false;

        Reflections reflections = new Reflections("", new SubTypesScanner(false), Thread.currentThread().getContextClassLoader());
        Set<Class<? extends BaseBundle>> bundlesClasses = reflections.getSubTypesOf(BaseBundle.class);

        for (Class<? extends BaseBundle> bundle : bundlesClasses) {
            if (!Modifier.isAbstract(bundle.getModifiers())) {
                DIFactory.registerNameSpace(bundle.getPackageName());
                unsortedBundleList.add((BaseBundleInterface)diInstance.create(bundle));
            }
        }

        // Create sorted bundle list by dependencies
        do {
            needProcess = false;

            for(BaseBundleInterface bundle : unsortedBundleList) {
                if (bundle.getDependencies().size() == 0 || _isAllParentsInList(bundle.getDependencies(), bundles)) {
                    bundles.add(bundle);
                    needProcess = true;
                }
            }

            unsortedBundleList.removeAll(bundles);
        } while(needProcess && unsortedBundleList.size() > 0);

        // If there left conflicting bundles with not resolved dependencies
        for(BaseBundleInterface bundle : unsortedBundleList) {
            bundles.add(bundle);
            unsortedBundleList.remove(bundle);
        }
    }

    /**
     * Check, if all parents already in sorted list
     *
     * @return boolean
     */
    private boolean _isAllParentsInList(List<Class<? extends BaseBundleInterface>> findWhat, List<BaseBundleInterface> findWhere) {
        int found = 0;

        for (Class<? extends BaseBundleInterface> what : findWhat) {
            for (BaseBundleInterface where : findWhere) {
                if (what.isAssignableFrom(where.getClass())) {
                    found++;
                    break;
                }
            }
        }

        return found == findWhat.size();
    }

    /**
     * Register bundles
     */
    private void _initializeBundles() {
        for(BaseBundleInterface bundle : bundles) {
            _initializeComponents(bundle);
        }
    }

    /**
     * Initialize component, add bindings
     */
    private void _initializeComponents(BaseBundleInterface bundle) {
        Reflections reflections = new Reflections(bundle.getClass().getPackageName(), new SubTypesScanner(false), Thread.currentThread().getContextClassLoader());
        Set<Class<? extends BaseComponent>> componentsClassList = reflections.getSubTypesOf(BaseComponent.class);
        BaseComponent component;

        for (Class<? extends BaseComponent> componentClass : componentsClassList) {
            component = (BaseComponent)diInstance.create(componentClass);
            components.add(component);
            eventsManager.registerScope(componentClass.getPackageName());

            for (Class<? extends BaseComponentInterface> componentParent : component.getParents()) {
                eventsManager.registerScope(componentClass.getPackageName(), componentParent.getPackageName());
            }
        }
    }
}
