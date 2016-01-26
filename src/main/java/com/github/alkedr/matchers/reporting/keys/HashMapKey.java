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
    public ExtractionResult extractFrom(Object item) throws MissingException, BrokenException {
        try {
            if (item == null || !((Map<?, ?>) item).containsKey(key)) {
                throw new MissingException(this);
            }
            return new ExtractionResult(this, ((Map<?, ?>) item).get(key));
        } catch (ClassCastException e) {
            throw new BrokenException(this, e);
        }
    }

    @Override
    public ExtractionResult extractFromMissingItem() throws MissingException, BrokenException {
        throw new MissingException(this);
    }
}
