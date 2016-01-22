package com.github.alkedr.matchers.reporting.extractors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ExtractingMatcherExtractors {
    public static Extractor fieldExtractor(Field field) {
        return new FieldExtractor(field);
    }

    public static Extractor fieldByNameExtractor(String fieldName) {
        return new FieldByNameExtractor(fieldName);
    }

    public static Extractor methodExtractor(Method method, Object... arguments) {
        return new MethodExtractor(method, arguments);
    }

    public static Extractor methodByNameExtractor(String methodName, Object... arguments) {
        return new MethodByNameExtractor(methodName, arguments);
    }

    public static Extractor getterExtractor(Method method) {
        return new GetterExtractor(method);
    }

    public static Extractor getterByNameExtractor(String methodName) {
        return new GetterByNameExtractor(methodName);
    }

    public static Extractor elementExtractor(int index) {
        return new ElementExtractor(index);
    }

    // TODO: hashMapEntryExtractor, в ReportingMatchers удобная объёртка valueForKey() ?
    public static Extractor valueForKeyExtractor(Object key) {
        return new ValueForKeyExtractor(key);
    }
}
