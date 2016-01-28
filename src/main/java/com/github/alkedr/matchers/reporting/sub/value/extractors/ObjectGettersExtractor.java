package com.github.alkedr.matchers.reporting.sub.value.extractors;

import java.lang.reflect.Method;
import java.util.Objects;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.getterKey;
import static java.lang.Character.isUpperCase;

class ObjectGettersExtractor implements SubValuesExtractor<Object> {
    private static final Method objectGetClassMethod;

    static final ObjectGettersExtractor INSTANCE = new ObjectGettersExtractor();

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
    public void run(Object item, SubValuesListener subValuesListener) {
        for (Method method : item.getClass().getMethods()) {
            if (isGetter(method)) {
                getterKey(method).run(item, subValuesListener);
            }
        }
    }

    @Override
    public void runForMissingItem(SubValuesListener subValuesListener) {
    }

    private static boolean isGetter(Method method) {
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
