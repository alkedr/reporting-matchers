package com.github.alkedr.matchers.reporting.keys;

public interface Key {
    String asString();
    @Override boolean equals(Object other);
    @Override int hashCode();
}
