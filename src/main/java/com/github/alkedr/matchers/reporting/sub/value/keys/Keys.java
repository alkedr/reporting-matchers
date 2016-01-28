package com.github.alkedr.matchers.reporting.sub.value.keys;

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

    public static ExtractableKey methodByNameKey(String methodName, Class<?>[] argumentClasses, Object... arguments) {
        return new MethodByNameKey(methodName, argumentClasses, arguments);
    }

    public static ExtractableKey methodKey(Method method, Object... arguments) {
        return new MethodKey(method, arguments);
    }

    public static ExtractableKey getterByNameKey(String methodName) {
        return renamedExtractableKey(methodByNameKey(methodName), MethodNameUtils.createNameForGetterMethodInvocation(methodName));
    }

    public static ExtractableKey getterKey(Method method) {
        return renamedExtractableKey(methodKey(method), MethodNameUtils.createNameForGetterMethodInvocation(method.getName()));
    }

    public static ExtractableKey renamedExtractableKey(ExtractableKey originalKey, String name) {
        return new RenamedKey(originalKey, name);
    }

    public static Key renamedKey(Key originalKey, String name) {
        return new RenamedKey(originalKey, name);
    }

    // TODO: обёртка, которая запрещает мёржить (в основном для methodKey, methodByNameKey и hashMapKey)
    // TODO: customKey(name, lambda) ?
}
