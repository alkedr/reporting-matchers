package com.github.alkedr.matchers.reporting.extraction;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class LambdaUtils {
    private static final Unsafe UNSAFE;

    static {
        try {
            Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
            unsafeConstructor.setAccessible(true);
            UNSAFE = unsafeConstructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }


    // Могут возникнуть проблемы с кастами внутри function


    public static <T> Iterable<ExtractingMatcher.Extractor> getExtractorsChainFromFunction(Function<T, Object> function) {
        Class<?> clazz = getArgumentClassFromFunction(function);
        Map<Object, Supplier<Iterable<ExtractingMatcher.Extractor>>> returnedValueToFieldSupplierMap =
                new IdentityHashMap<>();
        Object instance = createMockInstance(clazz, returnedValueToFieldSupplierMap);
        Object functionResult = function.apply((T) instance);
        if (returnedValueToFieldSupplierMap.containsKey(functionResult)) {
            return returnedValueToFieldSupplierMap.get(functionResult).get();
        }
        throw new RuntimeException("function не возвращает поле");  // FIXME
    }

    private static Object createMockInstance(Class<?> clazz, Map<Object, Supplier<Iterable<ExtractingMatcher.Extractor>>> returnedValueToFieldSupplierMap) {
        Object result = createUninitializedObject(clazz);
//        initializeFields(result, returnedValueToFieldSupplierMap);
        return result;
    }

    private static Object createUninitializedObject(Class<?> clazz) {
        try {
            return UNSAFE.allocateInstance(clazz);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }


    static <T> Class<?> getArgumentClassFromFunction(Function<T, Object> function) {
        try {
            function.apply((T) new Object());
            return Object.class;
        } catch (ClassCastException e) {
            // message исключения имеет вид 'java.lang.Object cannot be cast to com.my.package.MyCorrectClass'
            String[] split = e.getMessage().split(" ");
            try {
                return Class.forName(split[split.length-1]);
            } catch (ClassNotFoundException e1) {
                throw new RuntimeException(e1);  //FIXME
            }
        }
    }


    public static <T> Field getFieldFromFunction(Function<T, Object> function) {
        Class<?> clazz = getArgumentClassFromFunction(function);
        Object clazzInstance = null;
        Map<Object, Supplier<Field>> returnedValueToFieldSupplierMap = null;   // TODO: IdentityHashMap
        Object functionResult = function.apply((T) clazzInstance);
        if (returnedValueToFieldSupplierMap.containsKey(functionResult)) {
            return returnedValueToFieldSupplierMap.get(functionResult).get();
        }
        throw new RuntimeException("function не возвращает поле");  // FIXME
    }
}
