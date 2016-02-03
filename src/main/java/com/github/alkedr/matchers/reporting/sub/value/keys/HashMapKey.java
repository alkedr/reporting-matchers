package com.github.alkedr.matchers.reporting.sub.value.keys;

// не подходит для TreeMap, IdentityHashMap и пр.
// подходит только для HashMap (использует key.equals() и key.hashCode())
// TODO: иметь возможность настраивать способ преобразования ключа в строку?
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
