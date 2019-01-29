package dock.app.bundles.core.test.models.observers;

import dock.framework.models.components.events.interfaces.EventObserverInterface;

public class Observer implements EventObserverInterface {

    @dock.framework.models.components.events.anotations.Observer("aaaa")
    public void aaa() {
        System.out.println("abc");
    }
}
