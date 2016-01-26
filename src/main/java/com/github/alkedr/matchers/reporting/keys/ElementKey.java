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
    public ExtractionResult extractFrom(Object item) throws MissingException, BrokenException {
        if (item == null) {
            throw new MissingException(this);
        }
        if (item instanceof Object[]) {
            return extractFromArray((Object[]) item);
        }
        if (item instanceof List) {
            return extractFromList((List<?>) item);
        }
        if (item instanceof Iterable) {
            return extractFromIterable((Iterable<?>) item);
        }
        throw new BrokenException(this, new ClassCastException());  // FIXME ClassCastException? своё исключение?
    }

    private ExtractionResult extractFromArray(Object... array) throws MissingException {
        if (index < 0 || index >= array.length) {
            throw new MissingException(this);
        }
        return new ExtractionResult(this, array[index]);
    }

    private ExtractionResult extractFromList(List<?> list) throws MissingException {
        if (index < 0 || index >= list.size()) {
            throw new MissingException(this);
        }
        return new ExtractionResult(this, list.get(index));
    }

    private ExtractionResult extractFromIterable(Iterable<?> iterable) throws MissingException {
        Iterator<?> iterator = iterable.iterator();
        int currentIndex = 0;
        while (iterator.hasNext()) {
            Object currentElement = iterator.next();
            if (currentIndex == index) {
                return new ExtractionResult(this, currentElement);
            }
        }
        throw new MissingException(this);
    }

    @Override
    public ExtractionResult extractFromMissingItem() throws MissingException, BrokenException {
        throw new MissingException(this);
    }
}
