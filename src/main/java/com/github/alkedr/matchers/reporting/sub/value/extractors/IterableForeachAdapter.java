package com.github.alkedr.matchers.reporting.sub.value.extractors;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.ForeachAdapters.iteratorForeachAdapter;

class IterableForeachAdapter implements SubValuesExtractor<Iterable<?>> {
    static final IterableForeachAdapter INSTANCE = new IterableForeachAdapter();

    private IterableForeachAdapter() {
    }

    @Override
    public void run(Iterable<?> item, SubValuesListener subValuesListener) {
        iteratorForeachAdapter().run(item.iterator(), subValuesListener);
    }

    @Override
    public void runForMissingItem(SubValuesListener subValuesListener) {
    }
}
