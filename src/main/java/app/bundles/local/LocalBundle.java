package app.bundles.local;

import app.bundles.core.CoreBundle;
import dock.framework.BaseBundle;

public class LocalBundle extends BaseBundle {
    public LocalBundle() {
        setName("Core bundle");
        setDescription("Core bundle, used as base framework level");

        addDependency(CoreBundle.class);
    }
}
