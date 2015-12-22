package com.github.alkedr.matchers.reporting.array;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;

public class ArrayElementExtractor implements ExtractingMatcher.Extractor {
    private final int index;

    public ArrayElementExtractor(int index) {
        this.index = index;
    }

    @Override
    public ExtractedValue extractFrom(Object item) {
        // TODO: missing - выходит за границы, BROKEN - не массив
        return ExtractedValue.normal(((Object[])item)[index]);
    }
}
