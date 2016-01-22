package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

// не объединяется с непереименованным Key
// TODO: объединяться с непереименованным Key если названия совпадают?
class RenamedExtractableKey implements ExtractableKey {
    private final ExtractableKey key;
    private final String name;

    RenamedExtractableKey(ExtractableKey key, String name) {
        Validate.notNull(key, "key");
        Validate.notBlank(name, "name");
        this.key = key;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenamedExtractableKey that = (RenamedExtractableKey) o;
        return key.equals(that.key) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String asString() {
        return name;
    }

    @Override
    public Result extractFrom(Object item) {
        return key.extractFrom(item);
    }

    @Override
    public Result extractFromMissingItem() {
        return key.extractFromMissingItem();
    }
}
