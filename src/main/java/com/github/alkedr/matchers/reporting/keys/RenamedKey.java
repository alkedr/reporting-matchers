package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

// не объединяется с непереименованным Key
// TODO: объединяться с непереименованным Key если названия совпадают?
class RenamedKey implements Key {
    private final Key key;
    private final String name;

    RenamedKey(Key key, String name) {
        Validate.notNull(key, "key");
        Validate.notBlank(name, "name");
        this.key = key;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenamedKey that = (RenamedKey) o;
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
}
