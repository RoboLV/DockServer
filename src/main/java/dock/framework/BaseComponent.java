package dock.framework;

import dock.framework.interfaces.BaseBundleInterface;
import dock.framework.interfaces.BaseComponentInterface;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseComponent implements BaseComponentInterface {
    /**
     * Component name
     */
    private String name;

    /**
     * Component description
     */
    private String description;

    /**
     * Component version
     */
    private String version;

    /**
     * Component parents
     */
    private ArrayList<Class<? extends BaseComponentInterface>> parents = new ArrayList<>();

    public String getName() {
        return name;
    }

    protected BaseComponent setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    protected BaseComponent setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getVersion() {
        return version;
    }

    protected BaseComponent setVersion(String version) {
        this.version = version;
        return this;
    }

    public ArrayList<Class<? extends BaseComponentInterface>> getParents() {
        return parents;
    }

    protected BaseComponent setParent(ArrayList<Class<? extends BaseComponentInterface>> parents) {
        this.parents = parents;
        return this;
    }

    protected BaseComponent addParent(Class<? extends BaseComponentInterface> parent) {
        this.parents.add(parent);
        return this;
    }
}
