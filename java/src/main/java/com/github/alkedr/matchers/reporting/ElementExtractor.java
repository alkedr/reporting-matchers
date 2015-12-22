package com.github.alkedr.matchers.reporting;

import java.util.List;

public class ElementExtractor implements ExtractingMatcher.Extractor {
    private final int index;

    public ElementExtractor(int index) {
        this.index = index;
    }

    @Override
    public ExtractedValue extractFrom(Object item) {
        // TODO: missing - выходит за границы, BROKEN - не массив
        return ExtractedValue.normal(((List<?>)item).get(index));
    }
}
