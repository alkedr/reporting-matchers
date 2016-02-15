package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.apache.commons.lang3.Validate;

// не объединяется с непереименованным Key
// TODO: объединяться с непереименованным Key если названия совпадают?
class RenamedKey implements Key {
    private final Key originalKey;
    private final String name;

    RenamedKey(Key originalKey, String name) {
        Validate.notNull(originalKey, "originalKey");
        Validate.notBlank(name, "name");
        this.originalKey = originalKey;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenamedKey that = (RenamedKey) o;
        return originalKey.equals(that.originalKey) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return 31 * originalKey.hashCode() + name.hashCode();
    }

    @Override
    public String asString() {
        return name;
    }
}
