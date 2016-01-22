package com.github.alkedr.matchers.reporting.keys;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

// TODO: notMergeable(key) ?
public class Keys {

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

    public static Key getterKey(Method method) {
        return renamedKey(methodKey(method), createNameForGetterMethodInvocation(method.getName()));
    }

    public static Key getterByNameKey(String methodName) {
        return renamedKey(methodByNameKey(methodName), createNameForGetterMethodInvocation(methodName));
    }

    public static Key elementKey(int index) {
        return new ElementKey(index);
    }

    // подходит только для HashMap
    public static Key hashMapKey(Object key) {
        return new HashMapKey(key);
    }

    public static Key renamedKey(Key key, String name) {
        return new RenamedKey(key, name);
    }


    // TODO: что делать с очень большими toString()?
    static String createNameForRegularMethodInvocation(String methodName, Object... arguments) {
        StringBuilder sb = new StringBuilder();
        sb.append(methodName).append('(');
        if (arguments.length > 0) {
            sb.append(arguments[0]);
            for (int i = 1; i < arguments.length; i++) {
                sb.append(", ").append(arguments[i]);   // TODO: брать строки и символы в кавычки?
            }
        }
        sb.append(')');
        return sb.toString();
    }

    static String createNameForGetterMethodInvocation(String name) {
        if (name == null) {
            return "";
        }
        if (name.length() > 3 && name.startsWith("get") && isUpperCase(name.charAt(3))) {
            return toLowerCase(name.charAt(3)) + name.substring(4);
        }
        if (name.length() > 2 && name.startsWith("is") && isUpperCase(name.charAt(2))) {
            return toLowerCase(name.charAt(2)) + name.substring(3);
        }
        return name;
    }
}
