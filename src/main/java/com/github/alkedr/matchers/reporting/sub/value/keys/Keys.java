package com.github.alkedr.matchers.reporting.sub.value.keys;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public enum Keys {
    ;

    public static Key fieldKey(Field field) {
        return new FieldKey(field);
    }

    public static Key fieldByNameKey(String fieldName) {
        return new FieldByNameKey(fieldName);
    }

    public static Key methodKey(Method method, Object... arguments) {
        return new MethodKey(method, arguments);
    }

    public static Key methodByNameKey(String methodName, Object... arguments) {
        return new MethodByNameKey(methodName, arguments);
    }

    public static Key elementKey(int index) {
        return new ElementKey(index);
    }

    public static Key hashMapKey(Object key) {
        return new HashMapKey(key);
    }

    public static Key renamedKey(Key originalKey, String name) {
        return new RenamedKey(originalKey, name);
    }

    // TODO: обёртка, которая запрещает мёржить (в основном для methodKey, methodByNameKey и hashMapKey)
    // TODO: customKey(name, lambda) ?  customMergeableKey(name, lambda) ?
}
