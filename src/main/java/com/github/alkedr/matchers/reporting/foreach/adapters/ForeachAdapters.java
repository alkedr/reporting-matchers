package com.github.alkedr.matchers.reporting.foreach.adapters;

import java.util.Iterator;
import java.util.Map;

public class ForeachAdapters {
    public static ForeachAdapter<Iterator<?>> iteratorForeachAdapter() {
        return IteratorForeachAdapter.INSTANCE;
    }

    public static ForeachAdapter<Iterable<?>> iterableForeachAdapter() {
        return IterableForeachAdapter.INSTANCE;
    }

    public static ForeachAdapter<Object[]> arrayForeachAdapter() {
        return ArrayForeachAdapter.INSTANCE;
    }

    public static ForeachAdapter<Map<?, ?>> hashMapForeachAdepter() {
        return HashMapForeachAdepter.INSTANCE;
    }

    public static ForeachAdapter<Object> fieldsForeachAdepter() {
        return FieldsForeachAdapter.INSTANCE;
    }

    public static ForeachAdapter<Object> gettersForeachAdepter() {
        return GettersForeachAdapter.INSTANCE;
    }
}
