package com.github.alkedr.matchers.reporting.keys;

// не подходит для TreeMap, IdentityHashMap и пр.
class HashMapKey implements Key {
    private final Object key;

    HashMapKey(Object key) {
        this.key = key;
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
