package com.github.alkedr.matchers.reporting.keys;

import java.util.Map;

// не подходит для TreeMap, IdentityHashMap и пр.
// подходит только для HashMap
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
    public void extractFrom(Object item, ResultListener result) {
        try {
            if (item == null || !((Map<?, ?>) item).containsKey(key)) {
                result.missing(this);
            } else {
                result.present(this, ((Map<?, ?>) item).get(key));
            }
        } catch (ClassCastException e) {
            result.broken(this, e);
        }
    }

    @Override
    public void extractFromMissingItem(ResultListener result) {
        extractFrom(null, result);
    }
}
