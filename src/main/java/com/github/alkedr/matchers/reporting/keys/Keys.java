package com.github.alkedr.matchers.reporting.keys;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.keys.MethodNameUtils.createNameForGetterMethodInvocation;

// TODO: notMergeable(key) ?
public class Keys {
    public static ExtractableKey fieldKey(Field field) {
        return new FieldKey(field);
    }

    public static ExtractableKey fieldByNameKey(String fieldName) {
        return new FieldByNameKey(fieldName);
    }

    public static ExtractableKey methodKey(Method method, Object... arguments) {
        return new MethodKey(method, arguments);
    }

    public static ExtractableKey methodByNameKey(String methodName, Object... arguments) {
        return new MethodByNameKey(methodName, arguments);
    }

    public static ExtractableKey getterKey(Method method) {
        return renamedKey(methodKey(method), createNameForGetterMethodInvocation(method.getName()));
    }

    public static ExtractableKey getterByNameKey(String methodName) {
        return renamedKey(methodByNameKey(methodName), createNameForGetterMethodInvocation(methodName));
    }

    public static ExtractableKey elementKey(int index) {
        return new ElementKey(index);
    }

    // подходит только для HashMap
    // TODO: hashMapEntryKey, в ReportingMatchers удобная объёртка valueForKey() ?
    public static ExtractableKey hashMapKey(Object key) {
        return new HashMapKey(key);
    }

    public static Key renamedKey(Key key, String name) {
        return new RenamedKey(key, name);
    }

    public static ExtractableKey renamedKey(ExtractableKey key, String name) {
        return new RenamedExtractableKey(key, name);
    }
}
