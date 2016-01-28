package com.github.alkedr.matchers.reporting.foreach.adapters;

import java.util.Iterator;
import java.util.Map;

// TODO: 1 static инстанс
public class ForeachAdapters {
    public static ForeachAdapter<Iterator<?>> iteratorForeachAdapter() {
        return new IteratorForeachAdapter();
    }

    public static ForeachAdapter<Iterable<?>> iterableForeachAdapter() {
        return new IterableForeachAdapter();
    }

    public static ForeachAdapter<Object[]> arrayForeachAdapter() {
        return new ArrayForeachAdapter();
    }

    public static ForeachAdapter<Map<?, ?>> hashMapForeachAdepter() {
        return new HashMapForeachAdepter();
    }
}
