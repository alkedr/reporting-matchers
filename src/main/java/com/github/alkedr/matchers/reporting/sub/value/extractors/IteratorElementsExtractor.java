package com.github.alkedr.matchers.reporting.sub.value.extractors;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;

class IteratorElementsExtractor<T> implements SubValuesExtractor<Iterator<T>, T> {
    static final IteratorElementsExtractor INSTANCE = new IteratorElementsExtractor<>();

    private IteratorElementsExtractor() {
    }

    @Override
    public void run(Iterator<T> item, SubValuesListener<T> subValuesListener) {
        if (item != null) {
            int i = 0;
            while (item.hasNext()) {
                subValuesListener.present(elementKey(i++), item.next());
            }
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener<T> subValuesListener) {
    }
}
