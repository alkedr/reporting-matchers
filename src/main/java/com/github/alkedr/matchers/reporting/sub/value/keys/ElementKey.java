package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.apache.commons.lang3.Validate;

import java.util.Iterator;
import java.util.List;

class ElementKey implements ExtractableKey {
    private final int index;

    ElementKey(int index) {
        Validate.isTrue(index >= 0, "index must be greater than 0");
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementKey that = (ElementKey) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public String asString() {
        return "[" + (index + 1) + "]";
    }

    @Override
    public void run(Object item, SubValuesListener subValuesListener) {
        if (item == null) {
            subValuesListener.absent(this);
        } else if (item instanceof Object[]) {
            Object[] array = (Object[]) item;
            if (index < 0 || index >= array.length) {
                subValuesListener.absent(this);
            } else {
                subValuesListener.present(this, array[index]);
            }
        } else if (item instanceof List) {
            List<?> list = (List<?>) item;
            if (index < 0 || index >= list.size()) {
                subValuesListener.absent(this);
            } else {
                subValuesListener.present(this, list.get(index));
            }
        } else if (item instanceof Iterable) {
            Iterator<?> iterator = ((Iterable<?>) item).iterator();
            int currentIndex = 0;
            while (iterator.hasNext()) {
                Object currentElement = iterator.next();
                if (currentIndex == index) {
                    subValuesListener.present(this, currentElement);
                    return;
                }
            }
            subValuesListener.absent(this);
        } else {
            subValuesListener.broken(this, new ClassCastException());  // FIXME ClassCastException? своё исключение?
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener subValuesListener) {
        subValuesListener.absent(this);
    }
}
