package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.apache.commons.lang3.Validate;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;

public class ArrayElementExtractor<T> implements SubValuesExtractor<T[], T> {
    private final int index;

    public ArrayElementExtractor(int index) {
        Validate.isTrue(index >= 0, "index must be greater than 0");
        this.index = index;
    }

    @Override
    public void run(T[] item, SubValuesListener<T> subValuesListener) {
        if (item == null || index < 0 || index >= item.length) {
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
