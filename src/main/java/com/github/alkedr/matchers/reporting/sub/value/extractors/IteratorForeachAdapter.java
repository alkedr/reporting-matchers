package com.github.alkedr.matchers.reporting.sub.value.extractors;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;

class IteratorForeachAdapter implements SubValuesExtractor<Iterator<?>> {
    static final IteratorForeachAdapter INSTANCE = new IteratorForeachAdapter();

    private IteratorForeachAdapter() {
    }

    @Override
    public void run(Iterator<?> item, SubValuesListener subValuesListener) {
        int i = 0;
        while (item.hasNext()) {
            subValuesListener.present(elementKey(i++), item.next());
        }
    }

    @Override
    public void runForMissingItem(SubValuesListener subValuesListener) {
    }
}
