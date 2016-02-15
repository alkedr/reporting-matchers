package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.apache.commons.lang3.Validate;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;

public class IterableElementExtractor<T> implements SubValuesExtractor<Iterable<T>, T> {
    private final int index;

    public IterableElementExtractor(int index) {
        Validate.isTrue(index >= 0, "index must be greater than 0");
        this.index = index;
    }

    @Override
    public void run(Iterable<T> item, SubValuesListener<T> subValuesListener) {
        if (item != null) {
            Iterator<T> iterator = item.iterator();
            int currentIndex = 0;
            while (iterator.hasNext()) {
                T currentElement = iterator.next();
                if (currentIndex == index) {
                    subValuesListener.present(elementKey(index), currentElement);
                    return;
                }
            }
        }
        subValuesListener.absent(elementKey(index));
    }

    @Override
    public void runForAbsentItem(SubValuesListener<T> subValuesListener) {
        subValuesListener.absent(elementKey(index));
    }
}
