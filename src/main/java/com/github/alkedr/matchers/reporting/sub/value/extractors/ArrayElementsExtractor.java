package com.github.alkedr.matchers.reporting.sub.value.extractors;

import static java.util.Arrays.asList;

class ArrayElementsExtractor<T> implements SubValuesExtractor<T[], T> {
    static final ArrayElementsExtractor INSTANCE = new ArrayElementsExtractor<>();

    private ArrayElementsExtractor() {
    }

    @Override
    public void run(T[] item, SubValuesListener<T> subValuesListener) {
        if (item != null) {
            SubValuesExtractors.<T>iterableElementsExtractor().run(asList(item), subValuesListener);
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener<T> subValuesListener) {
    }
}
