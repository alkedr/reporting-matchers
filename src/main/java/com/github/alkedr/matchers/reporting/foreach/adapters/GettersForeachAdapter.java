package com.github.alkedr.matchers.reporting.foreach.adapters;

import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import com.github.alkedr.matchers.reporting.keys.Key;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiConsumer;

import static com.github.alkedr.matchers.reporting.keys.Keys.getterKey;
import static java.lang.Character.isUpperCase;

class GettersForeachAdapter implements ForeachAdapter<Object> {
    private static final Method objectGetClassMethod;

    static final GettersForeachAdapter INSTANCE = new GettersForeachAdapter();

    static {
        try {
            objectGetClassMethod = Object.class.getDeclaredMethod("getClass");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private GettersForeachAdapter() {
    }

    @Override
    public void run(Object item, BiConsumer<Key, Object> consumer) {
        // TODO: check null
        for (Method method : item.getClass().getMethods()) {
            if (isGetter(method)) {
                ExtractableKey key = getterKey(method);
                try {
                    consumer.accept(key, method.invoke(item));
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);  // FIXME
                }
            }
        }
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
