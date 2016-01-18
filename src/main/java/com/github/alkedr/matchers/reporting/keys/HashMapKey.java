package com.github.alkedr.matchers.reporting.keys;

import com.github.alkedr.matchers.reporting.ReportingMatcher;

import java.util.Objects;

// не подходит для TreeMap, IdentityHashMap и пр.
public class HashMapKey implements ReportingMatcher.Key {
    private final Object key;

    public HashMapKey(Object key) {
        this.key = key;
    }

    public Object getKey() {
        return key;
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
