package com.github.alkedr.matchers.reporting.keys;

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
    public void extractFrom(Object item, ResultListener result) {
        if (item == null) {
            result.missing(this);
        } else {
            if (item instanceof Object[]) {
                extractFromArray((Object[]) item, result);
            } else if (item instanceof List) {
                extractFromList((List<?>) item, result);
            } else if (item instanceof Iterable) {
                extractFromIterable((Iterable<?>) item, result);
            } else {
                result.broken(this, new ClassCastException());  // FIXME ClassCastException? своё исключение?
            }
        }
    }

    private void extractFromArray(Object[] array, ResultListener result) {
        if (index < 0 || index >= array.length) {
            result.missing(this);
        } else {
            result.present(this, array[index]);
        }
    }

    private void extractFromList(List<?> list, ResultListener result) {
        if (index < 0 || index >= list.size()) {
            result.missing(this);
        } else {
            result.present(this, list.get(index));
        }
    }

    private void extractFromIterable(Iterable<?> iterable, ResultListener result) {
        Iterator<?> iterator = iterable.iterator();
        int currentIndex = 0;
        while (iterator.hasNext()) {
            Object currentElement = iterator.next();
            if (currentIndex == index) {
                result.present(this, currentElement);
                return;
            }
        }
        result.missing(this);
    }

    @Override
    public void extractFromMissingItem(ResultListener result) {
        extractFrom(null, result);
    }
}
