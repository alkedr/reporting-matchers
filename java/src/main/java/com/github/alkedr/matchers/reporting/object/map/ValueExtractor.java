package com.github.alkedr.matchers.reporting.object.map;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;

import java.util.Map;

public class ValueExtractor<T> implements ExtractingMatcher.Extractor<T> {
    private final Object key;

    public ValueExtractor(Object key) {
        this.key = key;
    }

    @Override
    public ExtractedValue<T> extractFrom(Object item) {
        // TODO: missing, broken
        return ExtractedValue.normal(((Map<?, T>)item).get(key));
    }
}
