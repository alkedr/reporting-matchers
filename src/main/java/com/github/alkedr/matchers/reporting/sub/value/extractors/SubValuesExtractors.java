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


    public static <T, S> SubValuesExtractor<T, S> field(Field field) {
        return new FieldExtractor<>(field);
    }

    public static <T> SubValuesExtractor<T[], T> arrayElement(int index) {
        return new ArrayElementExtractor<>(index);
    }

    public static <T> SubValuesExtractor<Iterable<T>, T> iterableElement(int index) {
        return new IterableElementExtractor<>(index);
    }

    public static <T> SubValuesExtractor<List<T>, T> listElement(int index) {
        return new ListElementExtractor<>(index);
    }

    public static <T, S> SubValuesExtractor<T, S> fieldByName(String fieldName) {
        return new FieldByNameExtractor<>(fieldName);
    }

    public static <K, V> SubValuesExtractor<Map<K, V>, V> hashMap(K key) {
        return new HashMapExtractor<>(key);
    }

    public static <T, S> SubValuesExtractor<T, S> methodByName(String methodName, Object... arguments) {
        return new MethodByNameExtractor<>(methodName, arguments);
    }

    public static <T, S> SubValuesExtractor<T, S> method(Method method, Object... arguments) {
        return new MethodExtractor<>(method, arguments);
    }

    public static <T, S> SubValuesExtractor<T, S> getterByName(String methodName) {
        return renamed(methodByName(methodName), createNameForGetterMethodInvocation(methodName));
    }

    public static <T, S> SubValuesExtractor<T, S> getter(Method method) {
        return renamed(method(method), createNameForGetterMethodInvocation(method.getName()));
    }

    public static <T, S> SubValuesExtractor<T, S> renamed(SubValuesExtractor<T, S> originalExtractor, String name) {
        return new RenamedExtractor<>(originalExtractor, name);
    }


    public static <T> SubValuesExtractor<Iterator<T>, T> iteratorElements() {
        return IteratorElementsExtractor.INSTANCE;
    }

    public static <T> SubValuesExtractor<Iterable<T>, T> iterableElements() {
        return IterableElementsExtractor.INSTANCE;
    }

    public static <T> SubValuesExtractor<T[], T> arrayElements() {
        return ArrayElementsExtractor.INSTANCE;
    }

    public static <K, V> SubValuesExtractor<Map<K, V>, V> hashMapEntries() {
        return HashMapEntriesExtractor.INSTANCE;
    }

    public static <T> SubValuesExtractor<T, Object> fields() {
        return ObjectFieldsExtractor.INSTANCE;
    }

    public static <T> SubValuesExtractor<T, Object> getters() {
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
