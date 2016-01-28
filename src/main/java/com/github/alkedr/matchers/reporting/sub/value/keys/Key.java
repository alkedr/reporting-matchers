package com.github.alkedr.matchers.reporting.sub.value.keys;

// equals и hashCode используются для объединения
public interface Key {
    String asString();
    @Override boolean equals(Object other);
    @Override int hashCode();
}
