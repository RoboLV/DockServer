package dock.framework.utils.DI;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DIFactory {

    private static Map<Class, Class> diMap = new HashMap<Class, Class>(); // TODO: problem with many implementations

    private static Map<Class, Object> diSingletons = new HashMap<Class, Object>();

    /**
     * Initialize mapping
     */
    static {
        Reflections reflections = new Reflections("dock", new SubTypesScanner(false), Thread.currentThread().getContextClassLoader()); // TODO: create namespaces from config
        Set<Class<?>> types = reflections.getSubTypesOf(Object.class);

        for (Class<?> type : types) {
            Class<?>[] interfaces = type.getInterfaces();

            if (interfaces.length > 0) {
                for (Class<?> _interface : interfaces) {
                    diMap.put(_interface, type);
                }
            } else if(!type.isInterface()){
                diMap.put(type, type);
            }
        }
    }

    /**
     * Resolve object implementation, initialize object by passing arguments
     *
     * @param Class interfaceClass
     *
     * @return Object
     */
    public Object create(Class interfaceClass) {
        return create(interfaceClass, 0);
    }

    /**
     * Resolve object implementation, initialize object by passing arguments
     *
     * TODO: add ability to override bundles
     * TODO: Before class initialization, try resolve factory
     *
     * @param Class interfaceClass
     * @param int deep
     *
     * @return Object
     */
    public Object create(Class interfaceClass, int deep) {
        Class implementationClass = diMap.get(interfaceClass); // TODO: if many implementations, use model factory

        if (diSingletons.containsKey(implementationClass)) {
            return diSingletons.get(implementationClass);
        }

        if (implementationClass.isAnnotationPresent(DISingleton.class)) {
            synchronized (diSingletons) {
                Object obj = create(implementationClass, deep + 1);
                diSingletons.put(implementationClass, obj);

                return obj;
            }
        }

        try {
            if (implementationClass.isAnnotationPresent(DIInject.class) || deep > 0) {
                for(Constructor constructor : implementationClass.getDeclaredConstructors()) {
                    ArrayList<Object> args = new ArrayList<>();

                    for(Parameter parameter : constructor.getParameters()) {
                        args.add(create(parameter.getType(), deep + 1));
                    }

                    return implementationClass
                            .getDeclaredConstructor(constructor.getParameterTypes())
                            .newInstance((Object[])args.toArray());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}