package com.github.alkedr.matchers.reporting.sub.value.extractors;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValueExtractors.iterableForeachAdapter;
import static java.util.Arrays.asList;

class ArrayElementsExtractor implements SubValuesExtractor<Object[]> {
    static final ArrayElementsExtractor INSTANCE = new ArrayElementsExtractor();

    private ArrayElementsExtractor() {
    }

    @Override
    public void run(Object[] item, SubValuesListener subValuesListener) {
        iterableForeachAdapter().run(asList(item), subValuesListener);
    }

    @Override
    public void runForAbsentItem(SubValuesListener subValuesListener) {
    }
}
