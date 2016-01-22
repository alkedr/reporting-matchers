package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

import java.util.Iterator;
import java.util.List;

class ElementKey implements ExtractableKey {
    private final int index;

    ElementKey(int index) {
        Validate.isTrue(index >= 0, "index must be positive");
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
    public ExtractableKey.Result extractFrom(Object item) {
        if (item instanceof Object[]) {
            Object[] array = (Object[]) item;
            if (index < 0 || index >= array.length) {
                return new ExtractableKey.Result.Missing(this);
            }
            return new ExtractableKey.Result.Present(this, array[index]);
        }
        if (item instanceof List) {
            List<?> list = (List<?>) item;
            if (index < 0 || index >= list.size()) {
                return new ExtractableKey.Result.Missing(this);
            }
            return new ExtractableKey.Result.Present(this, list.get(index));
        }
        if (item instanceof Iterable) {
            Iterator<?> iterator = ((Iterable<?>) item).iterator();
            int currentIndex = 0;
            while (iterator.hasNext()) {
                Object currentElement = iterator.next();
                if (currentIndex == index) {
                    return new ExtractableKey.Result.Present(this, currentElement);
                }
            }
            return new ExtractableKey.Result.Missing(this);
        }
        return new ExtractableKey.Result.Broken(this, new ClassCastException());   // FIXME ClassCastException? своё исключение?
    }

    @Override
    public ExtractableKey.Result extractFromMissingItem() {
        return new ExtractableKey.Result.Missing(this);
    }
}
