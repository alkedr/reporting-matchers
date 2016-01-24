package com.github.alkedr.matchers.reporting.keys;

// не подходит для TreeMap, IdentityHashMap и пр.
// подходит только для HashMap
public class HashMapKey implements Key {
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
        if (o == null || getClass() != o.getClass()) return false;
        HashMapKey that = (HashMapKey) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String asString() {
        return String.valueOf(key);
    }
}
