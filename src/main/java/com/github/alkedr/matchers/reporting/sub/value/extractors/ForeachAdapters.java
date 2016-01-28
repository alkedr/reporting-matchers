package com.github.alkedr.matchers.reporting.sub.value.extractors;

import java.util.Iterator;
import java.util.Map;

public enum ForeachAdapters {
    ;

    public static SubValuesExtractor<Iterator<?>> iteratorForeachAdapter() {
        return IteratorForeachAdapter.INSTANCE;
    }

    public static SubValuesExtractor<Iterable<?>> iterableForeachAdapter() {
        return IterableForeachAdapter.INSTANCE;
    }

    public static SubValuesExtractor<Object[]> arrayForeachAdapter() {
        return ArrayForeachAdapter.INSTANCE;
    }

    public static SubValuesExtractor<Map<?, ?>> hashMapForeachAdapter() {
        return HashMapForeachAdapter.INSTANCE;
    }

    public static SubValuesExtractor<Object> fieldsForeachAdepter() {
        return FieldsForeachAdapter.INSTANCE;
    }

    public static SubValuesExtractor<Object> gettersForeachAdepter() {
        return GettersForeachAdapter.INSTANCE;
    }
}
