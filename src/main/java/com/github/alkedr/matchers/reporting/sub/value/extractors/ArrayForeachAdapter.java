package com.github.alkedr.matchers.reporting.sub.value.extractors;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.ForeachAdapters.iterableForeachAdapter;
import static java.util.Arrays.asList;

class ArrayForeachAdapter implements SubValuesExtractor<Object[]> {
    static final ArrayForeachAdapter INSTANCE = new ArrayForeachAdapter();

    private ArrayForeachAdapter() {
    }

    @Override
    public void run(Object[] item, SubValuesListener subValuesListener) {
        iterableForeachAdapter().run(asList(item), subValuesListener);
    }

    @Override
    public void runForMissingItem(SubValuesListener subValuesListener) {
    }
}
