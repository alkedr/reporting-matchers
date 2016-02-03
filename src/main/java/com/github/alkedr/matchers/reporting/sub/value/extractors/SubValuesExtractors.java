package com.github.alkedr.matchers.reporting.sub.value.extractors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

public enum SubValuesExtractors {
    ;


    public static <T, S> SubValuesExtractor<T, S> fieldExtractor(Field field) {
        return new FieldExtractor<>(field);
    }

    public static <T> SubValuesExtractor<T[], T> arrayElementExtractor(int index) {
        return new ArrayElementExtractor<>(index);
    }

    public static <T> SubValuesExtractor<Iterable<T>, T> iterableElementExtractor(int index) {
        return new IterableElementExtractor<>(index);
    }

    public static <T> SubValuesExtractor<List<T>, T> listElementExtractor(int index) {
        return new ListElementExtractor<>(index);
    }

    public static <T, S> SubValuesExtractor<T, S> fieldByNameExtractor(String fieldName) {
        return new FieldByNameExtractor<>(fieldName);
    }

    public static <K, V> SubValuesExtractor<Map<K, V>, V> hashMapExtractor(K key) {
        return new HashMapExtractor<>(key);
    }

    public static <T, S> SubValuesExtractor<T, S> methodByNameExtractor(String methodName, Object... arguments) {
        return new MethodByNameExtractor<>(methodName, arguments);
    }

    public static <T, S> SubValuesExtractor<T, S> methodExtractor(Method method, Object... arguments) {
        return new MethodExtractor<>(method, arguments);
    }

    public static <T, S> SubValuesExtractor<T, S> getterByNameExtractor(String methodName) {
        return renamedExtractor(methodByNameExtractor(methodName), createNameForGetterMethodInvocation(methodName));
    }

    public static <T, S> SubValuesExtractor<T, S> getterExtractor(Method method) {
        return renamedExtractor(methodExtractor(method), createNameForGetterMethodInvocation(method.getName()));
    }

    public static <T, S> SubValuesExtractor<T, S> renamedExtractor(SubValuesExtractor<T, S> originalExtractor, String name) {
        return new RenamedExtractor<>(originalExtractor, name);
    }



    public static <T> SubValuesExtractor<Iterator<T>, T> iteratorElementsExtractor() {
        return IteratorElementsExtractor.INSTANCE;
    }

    public static <T> SubValuesExtractor<Iterable<T>, T> iterableElementsExtractor() {
        return IterableElementsExtractor.INSTANCE;
    }

    public static <T> SubValuesExtractor<T[], T> arrayElementsExtractor() {
        return ArrayElementsExtractor.INSTANCE;
    }

    public static <K, V> SubValuesExtractor<Map<K, V>, V> hashMapEntriesExtractor() {
        return HashMapEntriesExtractor.INSTANCE;
    }

    public static <T> SubValuesExtractor<T, Object> objectFieldsExtractor() {
        return ObjectFieldsExtractor.INSTANCE;
    }

    public static <T> SubValuesExtractor<T, Object> objectGettersExtractor() {
        return ObjectGettersExtractor.INSTANCE;
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
