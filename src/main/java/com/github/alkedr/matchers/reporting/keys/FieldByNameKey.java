package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

class FieldByNameKey implements Key {
    private final String fieldName;

    FieldByNameKey(String fieldName) {
        Validate.notNull(fieldName, "fieldName");
        this.fieldName = fieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldByNameKey)) return false;
        FieldByNameKey that = (FieldByNameKey) o;
        return Objects.equals(fieldName, that.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName);
    }

    @Override
    public String asString() {
        return fieldName;
    }
}
