package com.github.alkedr.matchers.reporting.sub.value.extractors;

class IterableElementsExtractor<T> implements SubValuesExtractor<Iterable<T>, T> {
    static final IterableElementsExtractor INSTANCE = new IterableElementsExtractor<>();

    private IterableElementsExtractor() {
    }

    @Override
    public void run(Iterable<T> item, SubValuesListener<T> subValuesListener) {
        if (item != null) {
            SubValuesExtractors.<T>iteratorElements().run(item.iterator(), subValuesListener);
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener<T> subValuesListener) {
    }
}
