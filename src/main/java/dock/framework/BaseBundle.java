package dock.framework;

import dock.framework.interfaces.BaseBundleInterface;
import org.hibernate.TypeMismatchException;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseBundle implements BaseBundleInterface {
    /**
     * Bundle name
     */
    private String name;

    /**
     * Bundle description
     */
    private String description;

    /**
     * List of bundle dependencies.
     * Define load order, after which bundles, this bundle should be loaded
     */
    private ArrayList<Class<? extends BaseBundleInterface>> dependencies = new ArrayList<>();

    public String getName() {
        return name;
    }

    protected BaseBundle setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    protected BaseBundle setDescription(String description) {
        this.description = description;
        return this;
    }

    public ArrayList<Class<? extends BaseBundleInterface>> getDependencies() {
        return dependencies;
    }

    protected BaseBundle setDependencies(ArrayList<Class<? extends BaseBundleInterface>> dependencies) {
        this.dependencies = dependencies;
        return this;
    }

    protected BaseBundle addDependency(Class<? extends BaseBundleInterface> dependency) {
        if (!BaseBundleInterface.class.isAssignableFrom(dependency)) {
            throw new TypeMismatchException("Dependency should implement BaseBundleInterface");
        }

        this.dependencies.add(dependency);
        return this;
    }
}
