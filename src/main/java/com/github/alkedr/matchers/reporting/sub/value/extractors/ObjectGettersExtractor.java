package com.github.alkedr.matchers.reporting.sub.value.extractors;

import java.lang.reflect.Method;
import java.util.Objects;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.getter;
import static java.lang.Character.isUpperCase;
import static java.lang.reflect.Modifier.isStatic;

class ObjectGettersExtractor<T> implements SubValuesExtractor<T, Object> {
    private static final Method objectGetClassMethod;

    static final ObjectGettersExtractor INSTANCE = new ObjectGettersExtractor<>();

    static {
        try {
            objectGetClassMethod = Object.class.getDeclaredMethod("getClass");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private ObjectGettersExtractor() {
    }

    @Override
    public void run(T item, SubValuesListener<Object> subValuesListener) {
        if (item != null) {
            for (Method method : item.getClass().getMethods()) {
                if (isGetter(method)) {
                    getter(method).run(item, subValuesListener);
                }
            }
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener<Object> subValuesListener) {
    }

    private static boolean isGetter(Method method) {
        if (isStatic(method.getModifiers())) {
            return false;
        }
        if (method.getParameterCount() > 0) {
            return false;
        }
        if (method.getReturnType() == Void.TYPE) {
            return false;
        }
        if (!isGetterMethodName(method.getName(), method.getReturnType())) {
            return false;
        }
        if (Objects.equals(method, objectGetClassMethod)) {
            return false;
        }
        return true;
    }

    private static boolean isGetterMethodName(String name, Class<?> returnType) {
        if (name.startsWith("get")) {
            if (name.length() > 3 && isUpperCase(name.charAt(3))) {
                return true;
            }
        }
        if (name.startsWith("is")) {
            if (name.length() > 2 && isUpperCase(name.charAt(2)) && (returnType == boolean.class || returnType == Boolean.class)) {
                return true;
            }
        }
        return false;
    }
}
