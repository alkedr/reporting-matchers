package com.github.alkedr.matchers.reporting.keys;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public enum Keys {
    ;

    public static ExtractableKey elementKey(int index) {
        return new ElementKey(index);
    }

    public static ExtractableKey fieldByNameKey(String fieldName) {
        return new FieldByNameKey(fieldName);
    }

    public static ExtractableKey fieldKey(Field field) {
        return new FieldKey(field);
    }

    public static ExtractableKey hashMapKey(Object key) {
        return new HashMapKey(key);
    }

    public static ExtractableKey methodByNameKey(String methodName, Object... arguments) {
        return new MethodByNameKey(methodName, arguments);
    }

    public static ExtractableKey methodKey(Method method, Object... arguments) {
        return new MethodKey(method, arguments);
    }

    public static <K extends Key> Key renamedKey(K originalKey, String name) {
        return new RenamedKey<K>(originalKey, name);
    }
}
