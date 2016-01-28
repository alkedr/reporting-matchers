package com.github.alkedr.matchers.reporting.sub.value.extractors;

import java.util.Iterator;
import java.util.Map;

public enum SubValueExtractors {
    ;

    public static SubValuesExtractor<Iterator<?>> iteratorForeachAdapter() {
        return IteratorElementsExtractor.INSTANCE;
    }

    public static SubValuesExtractor<Iterable<?>> iterableForeachAdapter() {
        return IterableElementsExtractor.INSTANCE;
    }

    public static SubValuesExtractor<Object[]> arrayForeachAdapter() {
        return ArrayElementsExtractor.INSTANCE;
    }

    public static SubValuesExtractor<Map<?, ?>> hashMapForeachAdapter() {
        return HashMapEntriesExtractor.INSTANCE;
    }

    public static SubValuesExtractor<Object> fieldsForeachAdepter() {
        return ObjectFieldsExtractor.INSTANCE;
    }

    public static SubValuesExtractor<Object> gettersForeachAdepter() {
        return ObjectGettersExtractor.INSTANCE;
    }
}
