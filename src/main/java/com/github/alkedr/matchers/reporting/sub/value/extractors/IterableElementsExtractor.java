package com.github.alkedr.matchers.reporting.sub.value.extractors;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValueExtractors.iteratorForeachAdapter;

class IterableElementsExtractor implements SubValuesExtractor<Iterable<?>> {
    static final IterableElementsExtractor INSTANCE = new IterableElementsExtractor();

    private IterableElementsExtractor() {
    }

    @Override
    public void run(Iterable<?> item, SubValuesListener subValuesListener) {
        iteratorForeachAdapter().run(item.iterator(), subValuesListener);
    }

    @Override
    public void runForMissingItem(SubValuesListener subValuesListener) {
    }
}
