package com.github.alkedr.matchers.reporting.sub.value.extractors;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;

public class ArrayElementExtractor<T> implements SubValuesExtractor<T[], T> {
    private final int index;

    public ArrayElementExtractor(int index) {
        this.index = index;
    }

    @Override
    public void run(T[] item, SubValuesListener<T> subValuesListener) {
        if (index < 0 || index >= item.length) {
            subValuesListener.absent(elementKey(index));
        } else {
            subValuesListener.present(elementKey(index), item[index]);
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener<T> subValuesListener) {
        subValuesListener.absent(elementKey(index));
    }
}
