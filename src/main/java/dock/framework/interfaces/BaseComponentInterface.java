package dock.framework.interfaces;

import java.util.ArrayList;

public interface BaseComponentInterface {

    public String getName();

    public String getDescription();

    public String getVersion();

    public ArrayList<Class<? extends BaseComponentInterface>> getParents();
}
