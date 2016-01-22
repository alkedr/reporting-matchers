package com.github.alkedr.matchers.reporting.keys;

import java.util.Map;

// не подходит для TreeMap, IdentityHashMap и пр.
class HashMapKey implements ExtractableKey {
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

    @Override
    public Result extractFrom(Object item) {
        try {
            if (item == null || !((Map<?, ?>) item).containsKey(key)) {
                return new Result.Missing(this);
            }
            return new Result.Present(this, ((Map<?, ?>) item).get(key));
        } catch (ClassCastException e) {
            return new Result.Broken(this, e);
        }
    }

    @Override
    public Result extractFromMissingItem() {
        return new Result.Missing(this);
    }
}
