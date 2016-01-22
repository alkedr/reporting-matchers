package com.github.alkedr.matchers.reporting.keys;

import java.util.Objects;

// не должен объединяться с непереименованным Key
class RenamedKey implements Key {
    private final Key key;
    private final String name;

    RenamedKey(Key key, String name) {
        this.key = key;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RenamedKey)) return false;
        RenamedKey that = (RenamedKey) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, name);
    }

    @Override
    public String asString() {
        return name;
    }
}
