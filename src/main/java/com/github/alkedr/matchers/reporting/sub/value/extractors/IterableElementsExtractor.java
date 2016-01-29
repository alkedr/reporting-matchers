package com.github.alkedr.matchers.reporting.sub.value.extractors;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValueExtractors.iteratorElementsExtractor;

class IterableElementsExtractor implements SubValuesExtractor<Iterable<?>> {
    static final IterableElementsExtractor INSTANCE = new IterableElementsExtractor();

    private IterableElementsExtractor() {
    }

    @Override
    public void run(Iterable<?> item, SubValuesListener subValuesListener) {
        if (item != null) {
            iteratorElementsExtractor().run(item.iterator(), subValuesListener);
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener subValuesListener) {
    }
}
