package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.apache.commons.lang3.Validate;

import java.util.List;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;

public class ListElementExtractor<T> implements SubValuesExtractor<List<T>, T> {
    private final int index;

    public ListElementExtractor(int index) {
        Validate.isTrue(index >= 0, "index must be greater than 0");
        this.index = index;
    }

    @Override
    public void run(List<T> item, SubValuesExtractor.SubValuesListener<T> subValuesListener) {
        if (item == null || index < 0 || index >= item.size()) {
            subValuesListener.absent(elementKey(index));
        } else {
            subValuesListener.present(elementKey(index), item.get(index));
        }
    }

    @Override
    public void runForAbsentItem(SubValuesExtractor.SubValuesListener<T> subValuesListener) {
        subValuesListener.absent(elementKey(index));
    }
}
