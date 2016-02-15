package com.github.alkedr.matchers.reporting.sub.value.keys;

class HashMapValueForKeyKey implements Key {
    private final Object key;

    HashMapValueForKeyKey(Object key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashMapValueForKeyKey that = (HashMapValueForKeyKey) o;
        return key == null ? that.key == null : key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key == null ? 0 : key.hashCode();
    }

    @Override
    public String asString() {
        return String.valueOf(key);
    }
}
