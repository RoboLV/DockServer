package dock.framework.components.di;

import dock.framework.components.di.anotations.DISingleton;
import javassist.NotFoundException;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

@DISingleton
public class DIFactory {
    /**
     * Interface / realisation to exact implementation
     */
    private static Map<Class, ArrayList<Class>> diMap = new HashMap<>();

    /**
     * Singletons storage
     */
    private final static Map<Class, Object> diSingletons = new HashMap<>();

    /**
     * Abstract factory with auto resolve
     */
    private AbstractFactory abstractFactory;

    /**
     * Initialize mapping
     */
    static {
        registerNameSpace("dock");
    }

    /**
     * Add provided namespace to dependency injection
     *
     * @param nameSpace
     */
    public static void registerNameSpace(String nameSpace) {
        Reflections reflections = new Reflections(ClasspathHelper.forPackage(nameSpace), new SubTypesScanner(false));

        // TODO: Fix issue - it find outside package

        // Reflections reflections = new Reflections(nameSpace, new SubTypesScanner(false), Thread.currentThread().getContextClassLoader());
        Set<Class<?>> types = reflections.getSubTypesOf(Object.class);

        _processTypeRegistration(types);
    }

    /**
     * Register class in DI
     *
     * @param classType
     */
    public static void registerClass(Class<?> classType) {
        String pkg = classType.getPackage().getName();

        ConfigurationBuilder config = new ConfigurationBuilder()
                .setScanners(new ResourcesScanner(), new SubTypesScanner(false))
                .setUrls(ClasspathHelper.forPackage(pkg))
                .filterInputsBy(new FilterBuilder().includePackage(classType));
        Reflections reflections = new Reflections(config);

        // Reflections reflections = new Reflections(nameSpace, new SubTypesScanner(false), Thread.currentThread().getContextClassLoader());
        Set<Class<?>> types = reflections.getSubTypesOf(Object.class);

        _processTypeRegistration(types);
    }

    /**
     * Add collection to DI map
     *
     * @param types
     */
    protected static void _processTypeRegistration(Set<Class<?>> types) {
        for (Class<?> type : types) {
            if (!diMap.containsKey(type)) {
                diMap.put(type, new ArrayList<>());
            }

            if (!type.isInterface() && !diMap.get(type).contains(type)) {
                diMap.get(type).add(type);
            }

            Class<?>[] interfaces = type.getInterfaces();

            if (interfaces.length > 0) {
                for (Class<?> _interface : interfaces) {
                    if (!diMap.containsKey(_interface)) {
                        diMap.put(_interface, new ArrayList<>());
                    }

                    if (!diMap.get(_interface).contains(type)) {
                        diMap.get(_interface).add(type);
                    }
                }
            }
        }
    }

    /**
     * Resolve object implementation, initialize object by passing arguments
     *
     * @return Object
     */
    public Object create(Class interfaceClass) {
        return create(interfaceClass, 0);
    }

    /**
     * Resolve object implementation, initialize object by passing arguments
     *
     * @return Object
     */
    public Object create(Class interfaceClass, Object[] customArguments) {
        return create(interfaceClass, 0, customArguments);
    }

    /**
     * Resolve object implementation, initialize object by passing arguments
     *
     * @return Object
     */
    public Object create(Class interfaceClass, int deep) {
        return create(interfaceClass, deep, new Object[]{});
    }

    /**
     * Resolve object implementation, initialize object by passing arguments
     *
     * TODO: add ability to override bundles
     * TODO: Before class initialization, try resolve factory
     *
     * @return Object
     */
    public Object create(Class interfaceClass, int deep, Object[] customArguments) {
        Class implementationClass;

        if (deep > 5) {
            // TODO: throw new NotFoundException("Cannot register and initialize type");
            return null;
        }

        if (diMap.containsKey(interfaceClass)) {
            ArrayList<Class> implementations = diMap.get(interfaceClass);

            if (implementations.size() == 1) { // If only type or interface
                implementationClass = implementations.get(0);
            } else {
                implementationClass = implementations.get(0); // TODO: use factory
            }
        } else {
            registerClass(interfaceClass);
            return create(interfaceClass, deep + 1);
        }

        synchronized (diSingletons) {
            if (diSingletons.containsKey(implementationClass)) {
                return diSingletons.get(implementationClass);
            }

            if (implementationClass.isAnnotationPresent(DISingleton.class)) {

                Object obj = null;
                diSingletons.put(implementationClass, obj);

                try {
                    obj = _construct(implementationClass, customArguments);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                diSingletons.put(implementationClass, obj);

                return obj;
            }
        }

        try {
            return _construct(implementationClass, customArguments);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Initialize object;
     *
     * @param implementationClass
     * @return
     */
    protected Object _construct(Class implementationClass, Object[] customArguments) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object param;
        Object objectInstance;
        int argumentId;
        Boolean correct;

        for (Constructor constructor : implementationClass.getDeclaredConstructors()) {
            Object[] args = new Object[constructor.getParameters().length];
            argumentId = 0;
            correct = true;

            for (Parameter parameter : constructor.getParameters()) {
                if (customArguments.length > argumentId) {
                    param = customArguments[argumentId];

                    if (parameter.getType() != param.getClass()) {
                        correct = false;
                        break;
                    }
                } else {
                    param = create(parameter.getType());
                }

                if (!correct) {
                    break;
                }

                args[argumentId++] = param;
            }

            objectInstance = implementationClass
                    .getDeclaredConstructor(constructor.getParameterTypes())
                    .newInstance((Object[]) args);

            return objectInstance;
        }

        // TODO: throw exception invalid parameter types passed
        return null;
    }
}