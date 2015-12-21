package com.github.alkedr.matchers.reporting.object;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;

import java.lang.reflect.Method;

public class ObjectMethodByNameExtractor implements ExtractingMatcher.Extractor {
    private final String methodName;
    private final Object[] arguments;

    public ObjectMethodByNameExtractor(String methodName, Object... arguments) {
        this.methodName = methodName;
        this.arguments = arguments;
    }

    @Override
    public ExtractedValue extractFrom(Object item) {
        return null;
    }




    private static <T> Method getMethod(Class<T> tClass, String methodName, Object... arguments) {
        try {
            // TODO: проверить находит ли приватные, нужно ли вызывать setAccessible
            // TODO: MethodUtils.getAccessibleMethod?
            return tClass.getMethod(methodName, methodArgumentsToClasses(arguments));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e); // TODO: своё исключение
        }
    }

    private static Class<?>[] methodArgumentsToClasses(Object... arguments) {
        return null;
    }

}
