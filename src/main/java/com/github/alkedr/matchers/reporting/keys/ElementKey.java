package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

public class ElementKey implements Key {
    private final int index;

    public ElementKey(int index) {
        Validate.isTrue(index >= 0, "index must be positive");
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
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
