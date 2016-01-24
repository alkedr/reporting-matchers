package com.github.alkedr.matchers.reporting.keys;

// equals и hashCode используются для объединения
public interface Key {
    String asString();
    @Override boolean equals(Object other);
    @Override int hashCode();
}
