package dock.framework.components.di;

import dock.framework.components.di.anotations.DISingleton;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;

@DISingleton
public class DIFactory {

    private static Map<Class, ArrayList<Class>> diMap = new HashMap<>();

    private static Map<Class, Object> diSingletons = new HashMap<>();

    /**
     * Initialize mapping
     */
    static {
        Reflections reflections = new Reflections("dock", new SubTypesScanner(false), Thread.currentThread().getContextClassLoader()); // TODO: create namespaces from config
        Set<Class<?>> types = reflections.getSubTypesOf(Object.class);

        for (Class<?> type : types) {
            if (!diMap.containsKey(type)) {
                diMap.put(type, new ArrayList<>());
            }

            if (!type.isInterface()) {
                diMap.get(type).add(type);
            }

            Class<?>[] interfaces = type.getInterfaces();

            if (interfaces.length > 0) {
                for (Class<?> _interface : interfaces) {
                    if (!diMap.containsKey(_interface)) {
                        diMap.put(_interface, new ArrayList<>());
                    }

                    diMap.get(_interface).add(type);
                }
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
        Class implementationClass;

        if (diMap.containsKey(interfaceClass)) {
            ArrayList<Class> implementations = diMap.get(interfaceClass);

            if (implementations.size() == 1) { // If only type or interface
                implementationClass = implementations.get(0);
            } else {
                implementationClass = implementations.get(0); // TODO: use factory
            }
        } else {
            //throw new NullPointerException("Type not found"); // TODO:
            return null;
        }

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
            for(Constructor constructor : implementationClass.getDeclaredConstructors()) {
                ArrayList<Object> args = new ArrayList<>();

                for(Parameter parameter : constructor.getParameters()) {
                    args.add(create(parameter.getType(), deep + 1));
                }

                return implementationClass
                        .getDeclaredConstructor(constructor.getParameterTypes())
                        .newInstance((Object[])args.toArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}