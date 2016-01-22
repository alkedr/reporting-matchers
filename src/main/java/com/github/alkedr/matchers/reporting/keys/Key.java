package com.github.alkedr.matchers.reporting.keys;

public interface Key {
    String asString();
    @Override boolean equals(Object other);   // TODO: переименовать чтобы не забывать реализовывать?
    @Override int hashCode();
}
