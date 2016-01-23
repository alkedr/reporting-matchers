package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

// не объединяется с непереименованным Key
// TODO: объединяться с непереименованным Key если названия совпадают?
public class RenamedKey<K extends Key> implements Key {
    private final K originalKey;
    private final String name;

    public RenamedKey(K originalKey, String name) {
        Validate.notNull(originalKey, "originalKey");
        Validate.notBlank(name, "name");
        this.originalKey = originalKey;
        this.name = name;
    }

    public K getOriginalKey() {
        return originalKey;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenamedKey<?> that = (RenamedKey<?>) o;
        return originalKey.equals(that.originalKey) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = originalKey.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String asString() {
        return name;
    }
}
