package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.apache.commons.lang3.Validate;

class ElementKey implements Key {
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
}
