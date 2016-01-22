package com.github.alkedr.matchers.reporting.keys;

import java.util.Objects;

// не подходит для TreeMap, IdentityHashMap и пр.
class HashMapKey implements Key {
    private final Object key;

    HashMapKey(Object key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashMapKey)) return false;
        HashMapKey that = (HashMapKey) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String asString() {
        return String.valueOf(key);
    }
}
