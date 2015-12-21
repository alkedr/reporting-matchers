package com.github.alkedr.matchers.reporting.object.map;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;

import java.util.Map;

public class ValueExtractor implements ExtractingMatcher.Extractor {
    private final Object key;

    public ValueExtractor(Object key) {
        this.key = key;
    }

    @Override
    public ExtractedValue extractFrom(Object item) {
        // TODO: missing, broken
        return ExtractedValue.normal(((Map<?, ?>)item).get(key));
    }
}
