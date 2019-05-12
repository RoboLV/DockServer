package dock.framework.interfaces;

import java.util.ArrayList;

public interface BaseBundleInterface {

    public String getName();

    public String getDescription();

    public ArrayList<Class<? extends BaseBundleInterface>> getDependencies();
}
