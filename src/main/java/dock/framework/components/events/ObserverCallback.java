package dock.framework.components.events;

import java.lang.reflect.Method;

public class ObserverCallback {

    /**
     * Class key
     */
    protected Class aClass;

    /**
     * Method callback
     */
    protected Method callback;

    /**
     * Observer scope
     */
    protected String scope;

    public ObserverCallback(Class aClass, Method callback, String scope) {
        this.aClass = aClass;
        this.callback = callback;
        this.scope = scope;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Method getCallback() {
        return callback;
    }

    public void setCallback(Method callback) {
        this.callback = callback;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
