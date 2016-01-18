package com.github.alkedr.matchers.reporting.keys;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

public class ElementKey implements ReportingMatcher.Key {
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
        if (this == o) return true;
        if (!(o instanceof ElementKey)) return false;
        ElementKey that = (ElementKey) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public String asString() {
        return "[" + (index + 1) + "]";
    }
}
