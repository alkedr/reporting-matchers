package com.github.alkedr.matchers.reporting.object.list;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;

import java.util.List;

public class ElementExtractor<T> implements ExtractingMatcher.Extractor<T> {
    private final int index;

    public ElementExtractor(int index) {
        this.index = index;
    }

    @Override
    public ExtractedValue<T> extractFrom(Object item) {
        // TODO: missing - выходит за границы, BROKEN - не массив
        return ExtractedValue.normal(((List<T>)item).get(index));
    }
}
