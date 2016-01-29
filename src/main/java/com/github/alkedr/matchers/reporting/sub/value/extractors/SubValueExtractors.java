package com.github.alkedr.matchers.reporting.sub.value.extractors;

import java.util.Iterator;
import java.util.Map;

public enum SubValueExtractors {
    ;

    public static SubValuesExtractor<Iterator<?>> iteratorElementsExtractor() {
        return IteratorElementsExtractor.INSTANCE;
    }

    public static SubValuesExtractor<Iterable<?>> iterableElementsExtractor() {
        return IterableElementsExtractor.INSTANCE;
    }

    public static SubValuesExtractor<Object[]> arrayElementsExtractor() {
        return ArrayElementsExtractor.INSTANCE;
    }

    public static SubValuesExtractor<Map<?, ?>> hashMapEntriesExtractor() {
        return HashMapEntriesExtractor.INSTANCE;
    }

    public static SubValuesExtractor<Object> objectFieldsExtractor() {
        return ObjectFieldsExtractor.INSTANCE;
    }

    public static SubValuesExtractor<Object> objectGettersExtractor() {
        return ObjectGettersExtractor.INSTANCE;
    }
}
